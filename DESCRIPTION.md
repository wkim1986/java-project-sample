# CMD Execution
    chcp 65001
    java -jar -Dfile.encoding=UTF-8 Sample.jar
    
### chcp
- Active code page 변경 명령어
- 65001: UTF-8

# Intellij errors

### unmappable character for encoding x-windows-949
    settings -> 'encoding' 검색 -> [Editor] - [File Encodings] -> To UTF-8
    
### Console 한글 깨짐 현상
    [Help] - [Edit Custom VM Options...]
    
    Add in idea64.exe.vmoptios
    -Dfile.encoding=UTF-8
    -Dconsole.encoding=UTF-8
    
    Intellij restart
    