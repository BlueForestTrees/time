var webpack = require('webpack');

// Webpack Plugins
var autoprefixer = require('autoprefixer');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');

/**
 * Env
 * Get npm lifecycle event to identify the environment
 */
var ENV = process.env.npm_lifecycle_event;
var isProd = ENV === 'build';

module.exports = function makeWebpackConfig () {
	/**
	 * Config
	 * Reference: http://webpack.github.io/docs/configuration.html
	 * This is the object where all configuration gets set
	 */
	var config = {};

	// add debug messages
	config.debug = !isProd;

	/**
	 * Entry
	 * Reference: http://webpack.github.io/docs/configuration.html#entry
	 */
	config.entry = {
		index:  ['./src/js/app.js']
	};

	/**
	 * Output
	 * Reference: http://webpack.github.io/docs/configuration.html#output
	 */
	config.output = {
		path : __dirname + '/dist',
		publicPath : '/',
		filename : 'js/[name].[hash].js',
		chunkFilename : '[id].[hash].chunk.js'
	};

	/**
	 * Resolve
	 * Reference: http://webpack.github.io/docs/configuration.html#resolve
	 */
	config.resolve = {
		root : __dirname,
		// only discover files that have those extensions
		extensions : ['', '.js', '.json', '.css', '.html']
	};

	/**
	 * Loaders
	 * Reference: http://webpack.github.io/docs/configuration.html#module-loaders
	 * List: http://webpack.github.io/docs/list-of-loaders.html
	 * This handles most of the magic responsible for converting modules
	 */
	config.module = {
		preLoaders : [],
		loaders : [
			// Support for js
			// https://github.com/tcoopman/image-webpack-loader
			{
				test: /\.js$/,
				exclude: /node_modules/,
				loaders: []
			},
			// Support for minify images
			// https://github.com/tcoopman/image-webpack-loader
			{
				test: /\.(jpe?g|png|gif|svg|ico)$/i,
				loaders: [
					'file?hash=sha512&digest=hex&name=[name].[hash].[ext]',
					'image-webpack?bypassOnDebug&optimizationLevel=7&interlaced=false'
				]
			},

			// Support for *.json files.
			{test : /\.json$/, loader : 'json'},

			// Support for CSS as raw text
			{
				test : /\.css$/,
				exclude: /node_modules/,
				loaders : ['style', 'css']
			},
			
			// support for .html as raw text
			{test : /\.html$/, loader : 'raw'}
		],
		postLoaders : [],
		noParse : []
	};

	/**
	 * Plugins
	 * Reference: http://webpack.github.io/docs/configuration.html#plugins
	 * List: http://webpack.github.io/docs/list-of-plugins.html
	 */
	config.plugins = [
		// Define env variables to help with builds
		// Reference: https://webpack.github.io/docs/list-of-plugins.html#defineplugin
		new webpack.DefinePlugin({
			// Environment helpers
			'process.env' : {
				ENV : JSON.stringify(ENV)
			}
		}),

		// Inject script and link tags into html files
		// Reference: https://github.com/ampedandwired/html-webpack-plugin
		new HtmlWebpackPlugin({
			template : './src/public/index.html',
			inject : 'body'
		})
	];

	// Add build specific plugins
	if (isProd) {
		config.plugins.push(
			// Reference: http://webpack.github.io/docs/list-of-plugins.html#uglifyjsplugin
			// Minify all javascript, switch loaders to minimizing mode
			new webpack.optimize.UglifyJsPlugin(),

			// Copy assets from the public folder
			// Reference: https://github.com/kevlened/copy-webpack-plugin
			new CopyWebpackPlugin([{
				from : __dirname + '/src/public'
			}])
		);
	}

	/**
	 * PostCSS
	 * Reference: https://github.com/postcss/autoprefixer-core
	 * Add vendor prefixes to your css
	 */
	config.postcss = [
		autoprefixer({
			browsers : ['last 2 version']
		})
	];

	/**
	 * Dev server configuration
	 * Reference: http://webpack.github.io/docs/configuration.html#devserver
	 * Reference: http://webpack.github.io/docs/webpack-dev-server.html
	 */
	config.devServer = {
		contentBase : './src/public',
		historyApiFallback : false,
		stats : 'minimal' // none (or false), errors-only, minimal, normal (or true) and verbose
	};

	return config;
}();
