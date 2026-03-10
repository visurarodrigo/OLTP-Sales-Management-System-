@echo off
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202

echo Starting Spring Boot OLTP Sales System...
echo Using Java: %JAVA_HOME%
echo Application will be available at: http://localhost:8080
echo H2 Console will be available at: http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop the application
echo.

"C:\Program Files (x86)\Apache\maven\bin\mvn.cmd" spring-boot:run
