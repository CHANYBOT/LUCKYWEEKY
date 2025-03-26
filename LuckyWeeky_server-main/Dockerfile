# 1. Base Image
FROM tomcat:10.1.30-jdk17-temurin

# 2. Set timezone
ENV JAVA_OPTS="-Duser.timezone=Asia/Seoul"

# 3. Copy Maven build artifact to Tomcat webapps
COPY target/ROOT.war /usr/local/tomcat/webapps/

# 4. Expose application port
EXPOSE 8080

# 5. Start Tomcat
CMD ["catalina.sh", "run"]
