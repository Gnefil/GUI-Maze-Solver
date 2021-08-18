@echo off

rem Edit these if you're having issues
set JAVA=java.exe
set JAVAC=javac.exe
set HASWSL=

rem Test if we can use WSL for colour. Disable this if you are having issues.
wsl java.exe -version >NUL 2>&1 && (
    set HASWSL=true
)

goto :main
:title
    echo.
    echo [92m%*[0m
    exit /b
:subtitle
    echo [96m%*[0m
    exit /b
:main

rem Locate test types
SETLOCAL enabledelayedexpansion enableextensions
SET TEST_TYPES=
FOR /d %%i IN (src\tests\*) DO SET TEST_TYPES=!TEST_TYPES! %%~nxi
SET TEST_TYPES=%TEST_TYPES:~1%

rem Clean up
call :title Removing class files...
del /s /q *.class >NUL 2>&1
%JAVAC% -d ./bin --source-path ./src ./src/maze/Maze.java
%JAVAC% -d ./bin src/tests/ModifierChecker.java

rem @set isStatic=(%JAVA% -cp bin tests/ModifierChecker)
for /f %%i in ('%JAVA% -cp bin tests/ModifierChecker') do set isStatic=%%i
del /q *.class 2>NUL

rem Compile structural tests
call :title Compiling structural tests...

for %%i in (CoordinateTest, DirectionTest, ExceptionTest, MazeTest, RouteFinderTest, TileTest, VisualisationTest) do (
    for %%j IN (%TEST_TYPES%) do (
        call :subtitle Compiling tests/%%j/structural/%%i.java
        %JAVAC% -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src ./src/tests/%%j/structural/%%i.java
    )
)

rem Compile functional tests (these won't compile without code)
call :title Compiling functional tests...
for %%i in (MazeTest, RouteFinderTest, TileTest) do (
    for %%j IN (%TEST_TYPES%) do (
        call :subtitle Compiling tests/%%j/functional/%%i.java
        %JAVAC% -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src ./src/tests/%%j/functional/%%i.java
    )
)

:end

call :title Is static: %isStatic%
for %%j IN (%TEST_TYPES%) do (
    if "%isStatic%" == "true" (
        call :subtitle Compiling tests/%%j/functional/MazeCoordinateStaticTest.java
        %JAVAC% -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src src/tests/%%j/functional/MazeCoordinateStaticTest.java
    ) else (
        if "%isStatic%" == "false" (
            call :subtitle Compiling tests/%%j/functional/MazeCoordinateNotStaticTest.java
            %JAVAC% -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src src/tests/%%j/functional/MazeCoordinateNotStaticTest.java
        ) else (
            call :subtitle Compiling tests/%%j/functional/MazeCoordinateErrorTest.java
            %JAVAC% -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src src/tests/%%j/functional/MazeCoordinateErrorTest.java
        )
    )
)

rem All tests compiled, run whatever compiled OK
call :title Executing all compiled tests...
if "%HASWSL%" == "true" (
    wsl cmd.exe /k "java -jar junit-platform-console-standalone.jar --class-path ./bin --scan-class-path --fail-if-no-tests & exit"
) else (
    %JAVA% -jar junit-platform-console-standalone.jar --disable-ansi-colors --class-path ./bin --scan-class-path --fail-if-no-tests
)
