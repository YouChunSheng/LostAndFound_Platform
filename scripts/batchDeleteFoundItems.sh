#!/bin/bash

# 批量删除招领信息脚本

# 默认参数
BASE_URL=""
SESSION_ID=""
NUMBER=5
IDS=""

# 显示帮助信息
show_help() {
    echo "用法: $0 -u <基础URL> -s <会话ID> [-n <数量>] [--ids <ID列表>]"
    echo ""
    echo "选项:"
    echo "  -u, --url <URL>       应用的基础URL (例如: http://localhost:8080/lostandfound)"
    echo "  -s, --session <ID>    管理员会话ID"
    echo "  -n, --number <数量>   要删除的招领信息数量 (默认: 5)"
    echo "  --ids <ID列表>        要删除的招领信息ID列表 (例如: 1,2,3)"
    echo "  -h, --help            显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 -u http://localhost:8080/lostandfound -s ABC123XYZ -n 10"
    echo "  $0 -u http://localhost:8080/lostandfound -s ABC123XYZ --ids 1,2,3,4,5"
}

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -u|--url)
            BASE_URL="$2"
            shift 2
            ;;
        -s|--session)
            SESSION_ID="$2"
            shift 2
            ;;
        -n|--number)
            NUMBER="$2"
            shift 2
            ;;
        --ids)
            IDS="$2"
            shift 2
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            echo "未知选项: $1"
            show_help
            exit 1
            ;;
    esac
done

# 检查必需参数
if [ -z "$BASE_URL" ] || [ -z "$SESSION_ID" ]; then
    echo "错误: 必须提供基础URL和会话ID"
    show_help
    exit 1
fi

# 构造Python脚本调用参数
PYTHON_ARGS="-u $BASE_URL -s $SESSION_ID"

if [ -n "$IDS" ]; then
    # 将逗号分隔的ID转换为空格分隔
    IDS_SPACED=$(echo $IDS | tr ',' ' ')
    PYTHON_ARGS="$PYTHON_ARGS --ids $IDS_SPACED"
else
    PYTHON_ARGS="$PYTHON_ARGS -n $NUMBER"
fi

# 检查Python环境
if ! command -v python3 &> /dev/null; then
    echo "错误: 未找到Python3环境"
    exit 1
fi

# 执行批量删除操作
echo "正在执行批量删除操作..."
python3 "$(dirname "$0")/batchDeleteFoundItems.py" $PYTHON_ARGS