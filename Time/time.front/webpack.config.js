var webpack = require('webpack');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ENV = process.env.npm_lifecycle_event;
var isProd = ENV === 'build';
var srcDir = __dirname + "/src";
var destDir = __dirname + '/target';

module.exports = {
	debug: !isProd,
    entry: srcDir + "/app.js",
    output: {
		path : destDir,
		publicPath : '/',
		filename : 'js/time.[hash].js'
	},
    module: {
        loaders: [
            { test: /\.css$/, loader: "style!css" }
        ]
    },
	plugins: [
	    new CopyWebpackPlugin([{ from: srcDir + '/vendor', to: 'vendor'}]),
	    new CopyWebpackPlugin([{ from: srcDir + '/img', to: 'img'}]),
		new HtmlWebpackPlugin({template : srcDir + '/index.html',inject : 'body',hash : 'true'})
	],
    devServer: {
        port: 7072,
        proxy: {
            '/api/*': { target: 'http://localhost:8080' }
        },
    }
};

if(isProd){
    module.exports.plugins.push(new webpack.optimize.UglifyJsPlugin());
}