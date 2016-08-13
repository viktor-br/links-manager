docker build -t links-manager-server .
docker run -d -p 8080:8080 --link links-manager-mongo:links-manager-mongo -e SPRING_DATA_MONGODB_URI=mongodb://links-manager-mongo/links --name links-manager-server links-manager-server
docker run --name links-manager-mongo -d mongo
