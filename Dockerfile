FROM jamesdbloom/docker-java8-maven
MAINTAINER Strong Liu <stliu@apache.org>

RUN mkdir /var/projects
ADD src  /var/projects/src
ADD pom.xml /var/projects/pom.xml

RUN cd /var/projects; mvn clean install


EXPOSE 8080

WORKDIR /var/projects
ENTRYPOINT ["java -jar target/myproject-0.0.1-SNAPSHOT.jar --data.file.path=/var/github.data"]


#docker run -d -p 8080 --name $java_program $imagename -v host/var/log/:container/var/log