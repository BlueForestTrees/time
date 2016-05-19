var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ENV = process.env.npm_lifecycle_event;
var isProd = ENV === 'build';

console.log(ENV);

module.exports = {
	debug: !isProd,
    entry: "./app.js",
    output: {
		path : __dirname + '/target',
		publicPath : '/',
		filename : 'js/[name].[hash].js',
		chunkFilename : '[id].[hash].chunk.js'
	},
    module: {
        loaders: [
            { test: /\.css$/, loader: "style!css" }
        ]
    },
	plugins: [
		new HtmlWebpackPlugin({template : './index.html',inject : 'body',hash : 'true'}),
		new webpack.optimize.UglifyJsPlugin()
	]
};