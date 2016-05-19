var webpack = require('webpack');
var autoprefixer = require('autoprefixer');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var ENV = process.env.npm_lifecycle_event;
var isProd = ENV === 'build';

module.exports = function makeWebpackConfig () {
	var config = {};
	config.debug = !isProd;
	config.entry = { index:  ['./src/main/webapp/app.js'] };
	config.output = {
		path : __dirname + '/target/webapp',
		publicPath : '/',
		filename : 'js/[name].[hash].js',
		chunkFilename : '[id].[hash].chunk.js'
	};
	config.resolve = {
		root : __dirname + '/src/main/webapp',
		extensions : ['', '.js', '.json', '.css', '.html']
	};
	config.module = {
		preLoaders : [],
		loaders : [
			{ test : /\.js$/,   exclude: /node_modules/, loaders: [] },
			{ test : /\.json$/, loader : 'json' },
			{ test : /\.css$/,  loaders : ['style','css'] },
			{ test : /\.html$/, loader : 'raw'}
		],
		postLoaders : [],
		noParse : []
	};
	config.plugins = [
		new webpack.DefinePlugin({ 'process.env' : { ENV : JSON.stringify(ENV) } }),
		new HtmlWebpackPlugin({
			template : './src/main/webapp/view/histoires.html',
			inject : 'body',
			hash : 'true'
		})
	];
	if (isProd) {
		config.plugins.push(
			new webpack.optimize.UglifyJsPlugin(),
			new CopyWebpackPlugin([{from : __dirname + '/src/main/webapp/public'}])
		);
	}
	config.postcss = [autoprefixer({ browsers : ['last 2 version']})];
	config.devServer = {
		contentBase : './src/public',
		historyApiFallback : false,
		stats : 'minimal' // none (or false), errors-only, minimal, normal (or true) and verbose
	};
	return config;
}();
