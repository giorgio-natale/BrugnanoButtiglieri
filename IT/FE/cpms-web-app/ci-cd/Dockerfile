FROM nginx:stable-alpine
COPY build /usr/share/nginx/html
COPY ci-cd/default.conf /etc/nginx/conf.d
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]