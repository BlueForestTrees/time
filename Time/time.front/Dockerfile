FROM node:alpine AS histoires-front

RUN mkdir -p /build/front/workdir
COPY . /build/front/workdir
WORKDIR /build/front/workdir

ARG NODE_ENV
ENV NODE_ENV $NODE_ENV
RUN npm install
RUN npm run build

FROM tobi312/rpi-apache2
#FROM hypriot/rpi-busybox-httpd
#FROM httpd:alpine
#COPY ./httpd.conf /usr/local/apache2/conf/httpd.conf
COPY --from=histoires-front /build/front/workdir/target/ /var/www
COPY ./000-default.conf /etc/apache2/sites-available
COPY ./mods-available /etc/apache2/mods-enabled