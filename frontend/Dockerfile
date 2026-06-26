# ---- Etapa 1: Build de la app Angular ----
FROM node:20-alpine AS build

WORKDIR /app

# Copiamos primero los archivos de dependencias para aprovechar el cache de Docker
COPY package*.json ./
RUN npm install

# Copiamos el resto del código fuente
COPY . .

# Build de producción
RUN npm run build -- --configuration production

# ---- Etapa 2: Servir con Nginx ----
FROM nginx:alpine

# Output del nuevo builder de Angular (@angular/build:application):
# dist/<nombre-proyecto>/browser  -> en este caso "frontend-app"
COPY --from=build /app/dist/frontend-app/browser /usr/share/nginx/html

# Config de Nginx con fallback para el routing de Angular (SPA)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
