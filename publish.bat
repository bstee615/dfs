if [%1] == [] echo Please supply your net-ID e.g. "benjis" for benjis@iastate.edu && goto End

call mvn clean package
call mkdir publish
call mkdir publish\project2
call copy target\project2.jar publish\project2\ /y
call copy run.sh publish\project2\ /y
call copy testfile.txt publish\project2\ /y
call copy README.md publish\project2\ /y
call scp -r publish/project2 %1@pyrite.cs.iastate.edu:project2

:End