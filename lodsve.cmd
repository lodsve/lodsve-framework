@echo off

call start

:start
call shell/logo.cmd

if "%1"=="versions" (call shell/versions.cmd %2) else (
    if "%1"=="deploy" (call shell/deploy.cmd %2) else (
        goto help
    )
)
GOTO:EOF

:help
echo.----------------------------------
echo.
echo.usage: ./lodsve.bat [versions ^| deploy]
echo.
echo.-versions    Update lodsve-framework versions.
echo.-deploy      Deploy lodsve-framework to maven repository
echo.
echo.----------------------------------
GOTO:EOF