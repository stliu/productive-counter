FROM jamesdbloom/docker-java8-maven
MAINTAINER Strong Liu <stliu@apache.org>

RUN mvn clean install


EXPOSE 8080

ENTRYPOINT ["java -jar target/myproject-0.0.1-SNAPSHOT.jar --data.file.path=/var/github.data"]


#docker run -d -p 8080 --name $java_program $imagename -v host/var/log/:container/var/log