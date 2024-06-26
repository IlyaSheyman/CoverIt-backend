# **CoverIt**
### Application for AI-based generation of covers for playlists and music releases (tracks/albums).

## 👋Overview
You can generate a cover for a playlist based on several parameters: abstraction, image quality (Lo-fi / Hi-fi), and vibe - an alternative to the genre that determines the mood of the music. The algorithm analyzes track titles using ChatGPT and formulates a request to Dalle based on this.
![generate for playlist.jpg](images%2Fgenerate%20for%20playlist.jpg)
<br>
<br>
Result:
![generate auth.jpg](images%2Fgenerate%20auth.jpg)
<br>
<br>
You can also share playlists, sort them by different parameters, add them to your collection, and more.
![archive.png](images%2Farchive.png)
<br>
<br>
The application also provides the ability to generate a cover for a track: using a special constructor, which is converted by an internal algorithm into a prompt.
![generate for track.jpg](images%2Fgenerate%20for%20track.jpg)
<br>
<br>
Result:
![result for track.jpg](images%2Fresult%20for%20track.jpg)
<br>
<br>
The application is also accompanied by a Telegram bot that automatically sends server logs and information about certain user actions.
![logger.png](images%2Flogger.png)
<br>
<br>
## 🔧Configuration: [Swagger](https://api.cover-it.app/api-docs)
## 🚀Setup
### 1. Install Docker & Maven 
- [Download Docker Desktop](https://www.docker.com/products/docker-desktop/) and follow the installation instructions.
- Download Maven from [Apache Maven official website](https://maven.apache.org/index.html)
### 2. Running with Docker Compose
1. Clone the repository to your computer:
- `git clone <repository_url>`

2. Go to the project root directory:
- `cd CoverIt-backend`

3. Create a .env file with the required environment variables.

4. Run Maven package
- `mvn clean package`

5. Run Docker Compose:
- `docker-compose -f docker-compose_prod.yml up -d --build`

6. After successful launch, you will be able to access your application at:
- http://localhost:80 for Main Service

### Environment Variables

To run this project, you will need to add the following environment variables to your .env file.

**For the correct operation of the system of interaction with AI:**
- `OPENAI_SECRET_KEY`: OpenAI secret key.
  **For the correct operation of the Spotify Client:**
- `SPOTIFY_CLIENT_SECRET`: Spotify client secret.
- `SPOTIFY_CLIENT_ID`: Spotify client ID.
  **For the correct operation of the mail verification system:**
- `EMAIL_ADDRESS`: Email address.
- `EMAIL_APP_PASSWORD`: Application password for accessing the email account.
- `TOKEN_SIGNING_KEY`: Key for token signing.

**To save pictures to storage:**
- `CLOUD_NAME`: Cloud service name.
- `CLOUDINARY_API_KEY`: Cloudinary API key.
- `CLOUDINARY_API_SECRET`: Cloudinary API secret key.

**For the correct operation of the application subscription system:**
- `PATREON_ACCESS_TOKEN`: Access token for the Patreon API.
- `PATREON_CAMPAIGN_ID`: Campaign ID on Patreon.
- `PATREON_CLIENT_ID`: Patreon client ID.
- `PATREON_CLIENT_SECRET`: Patreon client secret key.
- `PATREON_REDIRECT_URI`: Redirect URI for Patreon authorization.

**DB & miscroservices configuration:**
- `IMAGE_SERVER_URL=http://image-server:9090`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://coverit-db:5432/coverit`
