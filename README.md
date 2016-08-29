```
# Run mongo container
docker run --name links-manager-mongo -d mongo
# Build and run docker container with app.
docker build -t links-manager-server .
docker run -d -p 8080:8080 --link links-manager-mongo:links-manager-mongo -e SPRING_DATA_MONGODB_URI=mongodb://links-manager-mongo/links --name links-manager-server links-manager-server
```

TODO

- User creation restriction
- Check user exist (if user logged-in, they will be able to use service till session expires)
- Add exception and its processing (proper codes for exceptions)
- Items search
- Versioning