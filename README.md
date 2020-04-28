# File analyzer

Console app to check the file type by searching specific patterns in file.

## How to start

```bash
git clone https://github.com/ljurak/file-analyzer.git
cd file-analyzer
mvn clean package
cd target
java -jar file-analyzer.jar file_with_patterns directory_with_files_to_check (e.g. java -jar file-analyzer patterns.txt test_files)
```