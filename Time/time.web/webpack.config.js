var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ENV = process.env.npm_lifecycle_event;
var isProd = false;//ENV === 'build';
var srcDir = __dirname + "/src/main/html.js";

module.exports = {
	debug: !isProd,
    entry: srcDir + "/app.js",
    output: {
		path : __dirname + '/target/webapp',
		publicPath : '/',
		filename : 'js/[name].[hash].js',
		chunkFilename : '[id].[hash].chunk.js'
	},
    module: {
        loaders: [
            { test: /\.css$/, loader: "style!css" }
        ]
    },commonschunkplugin
	plugins: [
		new HtmlWebpackPlugin({template : srcDir + '/index.html',inject : 'body',hash : 'true'})
	]
};

if(!isProd){
    module.exports.plugins.push(new webpack.optimize.UglifyJsPlugin());
}