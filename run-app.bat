@echo off
setlocal EnableExtensions EnableDelayedExpansion

set "REQUIRED_JAVA_MAJOR=21"
set "SELECTED_JAVA_HOME="

if defined JAVA_HOME (
	if exist "%JAVA_HOME%\bin\java.exe" (
		call :check_java "%JAVA_HOME%\bin\java.exe"
		if !errorlevel! equ 0 set "SELECTED_JAVA_HOME=%JAVA_HOME%"
	)
)

if not defined SELECTED_JAVA_HOME (
	for /d %%D in ("%ProgramFiles%\Java\jdk*" "%ProgramFiles%\Eclipse Adoptium\jdk*" "%ProgramFiles%\Microsoft\jdk*") do (
		if exist "%%~fD\bin\java.exe" (
			call :check_java "%%~fD\bin\java.exe"
			if !errorlevel! equ 0 (
				set "SELECTED_JAVA_HOME=%%~fD"
				goto :found_java
			)
		)
	)
)

:found_java
if not defined SELECTED_JAVA_HOME (
	echo [ERROR] Java %REQUIRED_JAVA_MAJOR%+ is required but was not found.
	echo [ERROR] Install Java 21 and set JAVA_HOME, then run this script again.
	exit /b 1
)

set "JAVA_HOME=%SELECTED_JAVA_HOME%"
set "PATH=%JAVA_HOME%\bin;%PATH%"

set "ACTIVE_JAVA_SPEC=21+"

set "MVN_CMD=%~dp0mvnw.cmd"
if not exist "%MVN_CMD%" set "MVN_CMD=C:\Program Files (x86)\Apache\maven\bin\mvn.cmd"
if not exist "%MVN_CMD%" set "MVN_CMD=mvn"

echo Starting Spring Boot OLTP Sales System...
echo Using Java: %JAVA_HOME% (spec %ACTIVE_JAVA_SPEC%)
echo Application will be available at: http://localhost:8080
echo H2 Console will be available at: http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop the application
echo.

call "%MVN_CMD%" spring-boot:run
exit /b %errorlevel%

:check_java
set "JAVA_EXE=%~1"
"%JAVA_EXE%" -version 2>&1 | findstr /R /C:"\"2[1-9]\." /C:"\"[3-9][0-9]\." >nul
if !errorlevel! equ 0 (
	exit /b 0
)

exit /b 1
