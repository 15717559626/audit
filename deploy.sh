#!/bin/bash
# 服务器端一键部署脚本
echo "========== 开始部署 audit 应用 =========="

# 设置变量
APP_NAME="audit"
CONTAINER_NAME="audit"
PORT="8080"
JAR_FILE="audit-0.0.1-SNAPSHOT.jar"
HEALTH_CHECK_URL="http://localhost:$PORT/hello?name=test"
MAX_WAIT_TIME=120
UPLOAD_DIR="/opt/uploads"
API_KEY_FILE="/root/.audit-api-key"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_success() { echo -e "${GREEN}✅ $1${NC}"; }
print_error()   { echo -e "${RED}❌ $1${NC}"; }
print_warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }
print_info()    { echo -e "${BLUE}ℹ️  $1${NC}"; }

# 0. 检查并生成 API Key（持久化，重启后不变）
echo "0. 检查 API Key..."
if [ ! -f "$API_KEY_FILE" ]; then
    API_KEY="sk-audit-$(date +%s)-$(head -c 16 /dev/urandom | base64 | tr -d '/+=')"
    echo "$API_KEY" > "$API_KEY_FILE"
    chmod 600 "$API_KEY_FILE"
    print_success "已生成新的 API Key: $API_KEY"
    print_warning "请妥善保管此 Key，调用方需要使用它"
else
    API_KEY=$(cat "$API_KEY_FILE")
    print_success "使用已有 API Key: $API_KEY"
fi

# 1. 检查必要文件
echo "1. 检查部署文件..."
if [ ! -f "$JAR_FILE" ]; then
    print_error "$JAR_FILE 文件不存在"
    echo "请先将 jar 文件上传到当前目录"
    exit 1
fi

if [ ! -f "Dockerfile" ]; then
    print_error "Dockerfile 文件不存在"
    echo "请先将 Dockerfile 上传到当前目录"
    exit 1
fi
print_success "部署文件检查完成"

# 2. 检查端口占用
echo "2. 检查端口占用..."
if netstat -tulpn 2>/dev/null | grep ":$PORT " | grep -v docker-proxy; then
    print_warning "端口 $PORT 被占用"
fi

# 3. 清理旧容器（兼容旧名称 audit-container）
echo "3. 清理旧的容器..."
for OLD_NAME in "$CONTAINER_NAME" "audit-container"; do
    if [ "$(docker ps -aq -f name=$OLD_NAME)" ]; then
        print_info "正在清理容器: $OLD_NAME ..."
        docker stop $OLD_NAME >/dev/null 2>&1
        docker rm $OLD_NAME >/dev/null 2>&1
    fi
done
print_success "旧容器已清理"

# 4. 清理旧镜像
echo "4. 清理旧的镜像..."
for OLD_IMG in "$APP_NAME" "audit-app"; do
    if [ "$(docker images -q $OLD_IMG 2>/dev/null)" ]; then
        docker rmi $OLD_IMG >/dev/null 2>&1
    fi
done
print_success "旧镜像已清理"

# 5. 构建镜像
echo "5. 构建 Docker 镜像..."
print_info "正在构建镜像，请稍候..."
if docker build -t $APP_NAME:latest . > build.log 2>&1; then
    print_success "Docker 镜像构建成功"
    rm -f build.log
else
    print_error "Docker 镜像构建失败"
    echo "构建日志："
    cat build.log
    rm -f build.log
    exit 1
fi

# 6. 创建上传目录
echo "6. 检查上传目录..."
if [ ! -d "$UPLOAD_DIR" ]; then
    mkdir -p $UPLOAD_DIR
    chmod 755 $UPLOAD_DIR
    print_success "上传目录已创建: $UPLOAD_DIR"
else
    print_success "上传目录已存在: $UPLOAD_DIR"
fi

# 7. 启动容器
echo "7. 启动 Docker 容器..."
if docker run -d \
  --name $CONTAINER_NAME \
  -p $PORT:8080 \
  --restart=unless-stopped \
  -e TZ=Asia/Shanghai \
  -e API_KEYS="$API_KEY" \
  --memory=1g \
  --memory-swap=1g \
  -v $UPLOAD_DIR:/opt/uploads \
  $APP_NAME:latest >/dev/null 2>&1; then
    print_success "容器启动成功"
else
    print_error "容器启动失败"
    docker logs $CONTAINER_NAME
    exit 1
fi

# 8. 健康检查
echo "8. 等待应用启动..."
print_info "最多等待 ${MAX_WAIT_TIME} 秒..."

elapsed=0
while [ $elapsed -lt $MAX_WAIT_TIME ]; do
    if docker ps --filter name=$CONTAINER_NAME --filter status=running -q | grep -q .; then
        if curl -s $HEALTH_CHECK_URL >/dev/null 2>&1; then
            print_success "应用健康检查通过"
            break
        elif [ $elapsed -gt 30 ]; then
            if docker logs $CONTAINER_NAME 2>&1 | grep -iE "started|running|ready|Tomcat" | tail -1 | grep -q .; then
                print_success "应用已启动完成"
                break
            fi
        fi
    else
        print_error "容器已停止运行"
        docker logs $CONTAINER_NAME
        exit 1
    fi
    sleep 3
    elapsed=$((elapsed + 3))
    printf "."
done
echo ""

if [ $elapsed -ge $MAX_WAIT_TIME ]; then
    print_warning "健康检查超时，请手动检查"
fi

# 9. 显示结果
echo "9. 部署状态检查..."
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo ""
    echo "=========================================="
    print_success "部署成功"
    echo "=========================================="
    echo ""
    echo "容器信息："
    docker ps --filter name=$CONTAINER_NAME --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    echo ""
    echo "API Key: $API_KEY"
    echo ""
    SERVER_IP=$(hostname -I | awk '{print $1}')
    echo "访问地址:"
    echo "   内网: http://$SERVER_IP:$PORT"
    echo "   本地: http://localhost:$PORT"
    PUBLIC_IP=$(curl -s --connect-timeout 3 ifconfig.me 2>/dev/null || echo "")
    if [ ! -z "$PUBLIC_IP" ] && [ "$PUBLIC_IP" != "$SERVER_IP" ]; then
        echo "   公网: http://$PUBLIC_IP:$PORT"
    fi
    echo ""
    echo "鉴权验证示例:"
    echo "   # 不带 Key（失败）"
    echo "   curl http://$SERVER_IP:$PORT/api/repair-diagnosis/list"
    echo ""
    echo "   # 带 Key（成功）"
    echo "   curl -H \"Authorization: Bearer $API_KEY\" http://$SERVER_IP:$PORT/api/repair-diagnosis/list"
else
    print_error "部署失败"
    echo "错误日志："
    docker logs $CONTAINER_NAME
    exit 1
fi

echo ""
echo "========== 部署完成 =========="
echo "常用命令："
echo "   查看日志:     docker logs -f $CONTAINER_NAME"
echo "   查看状态:     docker ps --filter name=$CONTAINER_NAME"
echo "   停止应用:     docker stop $CONTAINER_NAME"
echo "   启动应用:     docker start $CONTAINER_NAME"
echo "   重启应用:     docker restart $CONTAINER_NAME"
echo "   进入容器:     docker exec -it $CONTAINER_NAME /bin/bash"
echo "   查看 API Key: cat $API_KEY_FILE"
echo "   重新生成 Key: rm $API_KEY_FILE && bash deploy.sh"
echo ""
