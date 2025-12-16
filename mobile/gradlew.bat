@echo off
setlocal
set APP_HOME=%~dp0
set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

if not exist "%WRAPPER_JAR%" (
  echo Missing gradle wrapper jar: %WRAPPER_JAR%
  exit /b 1
)

set JAVA_CMD=java
if not "%JAVA_HOME%"=="" (
  if exist "%JAVA_HOME%\bin\java.exe" (
    set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
  )
)

%JAVA_CMD% -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
