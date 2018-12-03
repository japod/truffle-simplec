echo $(mvn -e -X -Dmaven.test.skip=true clean compile | grep -o -P '\-classpath .*? ' | awk '{print $2}')
