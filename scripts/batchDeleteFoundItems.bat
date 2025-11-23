@echo off
setlocal enabledelayedexpansion

:: 批量删除招领信息脚本

:: 默认参数
set BASE_URL=
set SESSION_ID=
set NUMBER=5
set IDS=

:: 解析命令行参数
:parse_args
if "%1"=="" goto start
if "%1"=="-u" set "BASE_URL=%2" & shift & shift & goto parse_args
if "%1"=="--url" set "BASE_URL=%2" & shift & shift & goto parse_args
if "%1"=="-s" set "SESSION_ID=%2" & shift & shift & goto parse_args
if "%1"=="--session" set "SESSION_ID=%2" & shift & shift & goto parse_args
if "%1"=="-n" set "NUMBER=%2" & shift & shift & goto parse_args
if "%1"=="--number" set "NUMBER=%2" & shift & shift & goto parse_args
if "%1"=="--ids" set "IDS=%2" & shift & shift & goto parse_args
if "%1"=="-h" goto help
if "%1"=="--help" goto help
shift
goto parse_args

:help
echo 用法: %0 -u ^<基础URL^> -s ^<会话ID^> [-n ^<数量^>] [--ids ^<ID列表^>]
echo.
echo 选项:
echo   -u, --url ^<URL^>       应用的基础URL (例如: http://localhost:8080/lostandfound)
echo   -s, --session ^<ID^>    管理员会话ID
echo   -n, --number ^<数量^>   要删除的招领信息数量 (默认: 5)
echo   --ids ^<ID列表^>        要删除的招领信息ID列表 (例如: 1,2,3)
echo   -h, --help            显示此帮助信息
echo.
echo 示例:
echo   %0 -u http://localhost:8080/lostandfound -s ABC123XYZ -n 10
echo   %0 -u http://localhost:8080/lostandfound -s ABC123XYZ --ids 1,2,3,4,5
goto end

:start
:: 检查必需参数
if "%BASE_URL%"=="" (
    echo 错误: 必须提供基础URL
    goto help
)

if "%SESSION_ID%"=="" (
    echo 错误: 必须提供会话ID
    goto help
)

:: 构造Python脚本调用参数
set PYTHON_ARGS=-u %BASE_URL% -s %SESSION_ID%

if defined IDS (
    :: 将逗号分隔的ID转换为空格分隔
    set IDS_SPACED=%IDS:,= %
    set PYTHON_ARGS=%PYTHON_ARGS% --ids %IDS_SPACED%
) else (
    set PYTHON_ARGS=%PYTHON_ARGS% -n %NUMBER%
)

:: 检查Python环境
where python >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Python环境
    exit /b 1
)

:: 执行批量删除操作
echo 正在执行批量删除操作...
python "%~dp0batchDeleteFoundItems.py" %PYTHON_ARGS%

:end