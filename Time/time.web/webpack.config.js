var webpack = require('webpack');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ENV = process.env.npm_lifecycle_event;
var isProd = ENV === 'build';
var srcDir = __dirname + "/src/main/html.js";
var destDir = __dirname + '/target/webapp';

module.exports = {
	debug: !isProd,
    entry: srcDir + "/app.js",
    output: {
		path : destDir,
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
	    new CopyWebpackPlugin([{ from: srcDir + '/vendor', to: 'vendor'}]),
	    new CopyWebpackPlugin([{ from: srcDir + '/img', to: 'img'}]),
		new HtmlWebpackPlugin({template : srcDir + '/index.html',inject : 'body',hash : 'true'})
	],
	proxy: {
      '/api/*': {
        target: 'localhost:8080'
      }
    },
    devServer: {
        port: 8081
    }
};

if(isProd){
    console.log("isProd so uglify");
    module.exports.plugins.push(new webpack.optimize.UglifyJsPlugin());
}