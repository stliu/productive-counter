FROM jamesdbloom/docker-java8-maven
MAINTAINER Strong Liu <stliu@apache.org>

RUN mkdir /var/projects
ADD src  /var/projects/src
ADD pom.xml /var/projects/pom.xml

RUN cd /var/projects; mvn clean install


EXPOSE 8080

CMD /usr/bin/java -jar /var/projects/target/myproject-0.0.1-SNAPSHOT.jar --data.file.path=/var/github/a.data

#docker build -t stliu/github:latest .

#docker run -p 8080   -v /home/core/github/:/var/github/ stliu/github
