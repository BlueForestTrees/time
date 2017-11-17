FROM node:alpine AS web

RUN mkdir -p /build/front/workdir
COPY . /build/front/workdir
WORKDIR /build/front/workdir

ARG NODE_ENV
ENV NODE_ENV $NODE_ENV
RUN npm install
RUN npm run build

FROM scratch
COPY --from=web /build/front/workdir/target/ /timefront/