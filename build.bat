@echo off
title SentinelCore Build Script
echo ========================================
echo Building SentinelCore by altblack...
echo ========================================

set MAVEN_HOME=C:\Program Files\Apache\maven\apache-maven-3.9.16
set PATH=%MAVEN_HOME%\bin;%PATH%

call mvn clean package

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Build failed! Please check the Maven logs above.
    pause
    exit /b %errorlevel%
)

echo.
echo [SUCCESS] Build completed successfully!
echo Look for the JAR file inside the 'target' folder.
echo ========================================
pause