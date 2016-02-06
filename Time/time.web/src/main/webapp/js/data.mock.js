(function() {

	var responseTimeBucket = 300;
	var responseTimePhrases = 100;

    function data() {

    }

    data.prototype.getBuckets = function(term, scale, callback) {
       if(scale === 0){
		    setTimeout(function(){
		        callback({"scale":"0","buckets":[{"bucket":0,"count":1120}]});
		    }, responseTimeBucket);
		}else if(scale === 1){
			setTimeout(function(){
				callback({"scale":"1","buckets":[{"bucket":73,"count":468},{"bucket":72,"count":189},{"bucket":71,"count":154},{"bucket":70,"count":79},{"bucket":69,"count":68},{"bucket":68,"count":61},{"bucket":67,"count":15},{"bucket":64,"count":13},{"bucket":65,"count":10},{"bucket":66,"count":10},{"bucket":61,"count":8},{"bucket":58,"count":7},{"bucket":56,"count":6},{"bucket":59,"count":4},{"bucket":62,"count":4},{"bucket":54,"count":3},{"bucket":57,"count":3},{"bucket":60,"count":3},{"bucket":63,"count":3},{"bucket":36,"count":2},{"bucket":-73,"count":1},{"bucket":120,"count":1},{"bucket":17,"count":1},{"bucket":187,"count":1},{"bucket":42,"count":1},{"bucket":45,"count":1},{"bucket":51,"count":1},{"bucket":52,"count":1},{"bucket":53,"count":1},{"bucket":55,"count":1}]});
			}, responseTimeBucket);
		}else if(scale === 2){
			setTimeout(function(){
				callback({"scale":"2","buckets":[{"bucket":1466,"count":68},{"bucket":1469,"count":61},{"bucket":1467,"count":55},{"bucket":1468,"count":53},{"bucket":1465,"count":51},{"bucket":1463,"count":37},{"bucket":1470,"count":37},{"bucket":1460,"count":27},{"bucket":1458,"count":24},{"bucket":1471,"count":21},{"bucket":1434,"count":18},{"bucket":1439,"count":18},{"bucket":1444,"count":18},{"bucket":1464,"count":18},{"bucket":1436,"count":17},{"bucket":1462,"count":17},{"bucket":1433,"count":16},{"bucket":1374,"count":15},{"bucket":1459,"count":15},{"bucket":1472,"count":14},{"bucket":1447,"count":13},{"bucket":1448,"count":13},{"bucket":1452,"count":13},{"bucket":1431,"count":12},{"bucket":1413,"count":11},{"bucket":1441,"count":11},{"bucket":1455,"count":11},{"bucket":1376,"count":10},{"bucket":1409,"count":10},{"bucket":1450,"count":10},{"bucket":1414,"count":9},{"bucket":1417,"count":9},{"bucket":1461,"count":9},{"bucket":1382,"count":8},{"bucket":1385,"count":8},{"bucket":1440,"count":8},{"bucket":1445,"count":8},{"bucket":1454,"count":8},{"bucket":1457,"count":8},{"bucket":1379,"count":7},{"bucket":1398,"count":7},{"bucket":1424,"count":7},{"bucket":1428,"count":7},{"bucket":1432,"count":7},{"bucket":1438,"count":7},{"bucket":1451,"count":7},{"bucket":1314,"count":6},{"bucket":1366,"count":6},{"bucket":1370,"count":6},{"bucket":1380,"count":6},{"bucket":1389,"count":6},{"bucket":1420,"count":6},{"bucket":1435,"count":6},{"bucket":1416,"count":5},{"bucket":1427,"count":5},{"bucket":1429,"count":5},{"bucket":1437,"count":5},{"bucket":1446,"count":5},{"bucket":1456,"count":5},{"bucket":1298,"count":4},{"bucket":1377,"count":4},{"bucket":1381,"count":4},{"bucket":1387,"count":4},{"bucket":1395,"count":4},{"bucket":1402,"count":4},{"bucket":1404,"count":4},{"bucket":1406,"count":4},{"bucket":1425,"count":4},{"bucket":1430,"count":4},{"bucket":1453,"count":4},{"bucket":1095,"count":3},{"bucket":1154,"count":3},{"bucket":1239,"count":3},{"bucket":1336,"count":3},{"bucket":1383,"count":3},{"bucket":1391,"count":3},{"bucket":1394,"count":3},{"bucket":1399,"count":3},{"bucket":1407,"count":3},{"bucket":1412,"count":3},{"bucket":1418,"count":3},{"bucket":1419,"count":3},{"bucket":1422,"count":3},{"bucket":1423,"count":3},{"bucket":1426,"count":3},{"bucket":1442,"count":3},{"bucket":1443,"count":3},{"bucket":1168,"count":2},{"bucket":1171,"count":2},{"bucket":1182,"count":2},{"bucket":1236,"count":2},{"bucket":1282,"count":2},{"bucket":1294,"count":2},{"bucket":1328,"count":2},{"bucket":1344,"count":2},{"bucket":1346,"count":2},{"bucket":1349,"count":2},{"bucket":1352,"count":2},{"bucket":1353,"count":2},{"bucket":1361,"count":2},{"bucket":1372,"count":2},{"bucket":1375,"count":2},{"bucket":1384,"count":2},{"bucket":1386,"count":2},{"bucket":1390,"count":2},{"bucket":1396,"count":2},{"bucket":1405,"count":2},{"bucket":1408,"count":2},{"bucket":1410,"count":2},{"bucket":1411,"count":2},{"bucket":1449,"count":2},{"bucket":730,"count":2},{"bucket":-1460,"count":1},{"bucket":1033,"count":1},{"bucket":1040,"count":1},{"bucket":1078,"count":1},{"bucket":1106,"count":1},{"bucket":1121,"count":1},{"bucket":1123,"count":1},{"bucket":1132,"count":1},{"bucket":1134,"count":1},{"bucket":1137,"count":1},{"bucket":1138,"count":1},{"bucket":1160,"count":1},{"bucket":1165,"count":1},{"bucket":1173,"count":1},{"bucket":1193,"count":1},{"bucket":1199,"count":1},{"bucket":1200,"count":1},{"bucket":1209,"count":1},{"bucket":1216,"count":1},{"bucket":1225,"count":1},{"bucket":1232,"count":1},{"bucket":1238,"count":1},{"bucket":1241,"count":1},{"bucket":1242,"count":1},{"bucket":1244,"count":1},{"bucket":1252,"count":1},{"bucket":1263,"count":1},{"bucket":1271,"count":1},{"bucket":1276,"count":1},{"bucket":1281,"count":1},{"bucket":1284,"count":1},{"bucket":1286,"count":1},{"bucket":1292,"count":1},{"bucket":1297,"count":1},{"bucket":1306,"count":1},{"bucket":1309,"count":1},{"bucket":1310,"count":1},{"bucket":1319,"count":1},{"bucket":1325,"count":1},{"bucket":1326,"count":1},{"bucket":1327,"count":1},{"bucket":1330,"count":1},{"bucket":1332,"count":1},{"bucket":1347,"count":1},{"bucket":1348,"count":1},{"bucket":1355,"count":1},{"bucket":1358,"count":1},{"bucket":1359,"count":1},{"bucket":1360,"count":1},{"bucket":1362,"count":1},{"bucket":1364,"count":1},{"bucket":1365,"count":1},{"bucket":1368,"count":1},{"bucket":1371,"count":1},{"bucket":1373,"count":1},{"bucket":1397,"count":1},{"bucket":1400,"count":1},{"bucket":1401,"count":1},{"bucket":1415,"count":1},{"bucket":1421,"count":1},{"bucket":2410,"count":1},{"bucket":348,"count":1},{"bucket":3752,"count":1},{"bucket":858,"count":1},{"bucket":900,"count":1}]});
			}, responseTimeBucket);
		}else if(scale === 3){
			setTimeout(function(){
				callback({"scale":"3","buckets":[{"bucket":73450,"count":19},{"bucket":73267,"count":18},{"bucket":73194,"count":16},{"bucket":73304,"count":16},{"bucket":73012,"count":15},{"bucket":73158,"count":15},{"bucket":73340,"count":14},{"bucket":73413,"count":14},{"bucket":71697,"count":13},{"bucket":73121,"count":13},{"bucket":73273,"count":12},{"bucket":72208,"count":11},{"bucket":72939,"count":11},{"bucket":73377,"count":11},{"bucket":71952,"count":10},{"bucket":72975,"count":10},{"bucket":71843,"count":9},{"bucket":72354,"count":9},{"bucket":71733,"count":8},{"bucket":71989,"count":8},{"bucket":73231,"count":8},{"bucket":68848,"count":7},{"bucket":70674,"count":7},{"bucket":72025,"count":7},{"bucket":72098,"count":7},{"bucket":72427,"count":7},{"bucket":73048,"count":7},{"bucket":73486,"count":7},{"bucket":65743,"count":6},{"bucket":68519,"count":6},{"bucket":68957,"count":6},{"bucket":69030,"count":6},{"bucket":70491,"count":6},{"bucket":70747,"count":6},{"bucket":71404,"count":6},{"bucket":71624,"count":6},{"bucket":71806,"count":6},{"bucket":72865,"count":6},{"bucket":72902,"count":6},{"bucket":73523,"count":6},{"bucket":68336,"count":5},{"bucket":68702,"count":5},{"bucket":70820,"count":5},{"bucket":71222,"count":5},{"bucket":71478,"count":5},{"bucket":71726,"count":5},{"bucket":72646,"count":5},{"bucket":72829,"count":5},{"bucket":73456,"count":5},{"bucket":68732,"count":4},{"bucket":68884,"count":4},{"bucket":69103,"count":4},{"bucket":69250,"count":4},{"bucket":70857,"count":4},{"bucket":71258,"count":4},{"bucket":71368,"count":4},{"bucket":71514,"count":4},{"bucket":71587,"count":4},{"bucket":71916,"count":4},{"bucket":72245,"count":4},{"bucket":72318,"count":4},{"bucket":72500,"count":4},{"bucket":72537,"count":4},{"bucket":72573,"count":4},{"bucket":72610,"count":4},{"bucket":73085,"count":4},{"bucket":73278,"count":4},{"bucket":73409,"count":4},{"bucket":73428,"count":4},{"bucket":73504,"count":4},{"bucket":73627,"count":4},{"bucket":54786,"count":3},{"bucket":57744,"count":3},{"bucket":61981,"count":3},{"bucket":68811,"count":3},{"bucket":69286,"count":3},{"bucket":69469,"count":3},{"bucket":69578,"count":3},{"bucket":69943,"count":3},{"bucket":70309,"count":3},{"bucket":70382,"count":3},{"bucket":70637,"count":3},{"bucket":70893,"count":3},{"bucket":71003,"count":3},{"bucket":71185,"count":3},{"bucket":71551,"count":3},{"bucket":71660,"count":3},{"bucket":71770,"count":3},{"bucket":71879,"count":3},{"bucket":72281,"count":3},{"bucket":72435,"count":3},{"bucket":72756,"count":3},{"bucket":72792,"count":3},{"bucket":72963,"count":3},{"bucket":73252,"count":3},{"bucket":73319,"count":3},{"bucket":73320,"count":3},{"bucket":73322,"count":3},{"bucket":73349,"count":3},{"bucket":73373,"count":3},{"bucket":73380,"count":3},{"bucket":73384,"count":3},{"bucket":73401,"count":3},{"bucket":73435,"count":3},{"bucket":73438,"count":3},{"bucket":73459,"count":3},{"bucket":73465,"count":3},{"bucket":73474,"count":3},{"bucket":73535,"count":3},{"bucket":73547,"count":3},{"bucket":73559,"count":3},{"bucket":73596,"count":3},{"bucket":36513,"count":2},{"bucket":59132,"count":2},{"bucket":61835,"count":2},{"bucket":64721,"count":2},{"bucket":64920,"count":2},{"bucket":64940,"count":2},{"bucket":66802,"count":2},{"bucket":67337,"count":2},{"bucket":67496,"count":2},{"bucket":67679,"count":2},{"bucket":68081,"count":2},{"bucket":68720,"count":2},{"bucket":68738,"count":2},{"bucket":69097,"count":2},{"bucket":69106,"count":2},{"bucket":69140,"count":2},{"bucket":69173,"count":2},{"bucket":69213,"count":2},{"bucket":69359,"count":2},{"bucket":69396,"count":2},{"bucket":69488,"count":2},{"bucket":69542,"count":2},{"bucket":69724,"count":2},{"bucket":69761,"count":2},{"bucket":69834,"count":2},{"bucket":69907,"count":2},{"bucket":69974,"count":2},{"bucket":70105,"count":2},{"bucket":70126,"count":2},{"bucket":70210,"count":2},{"bucket":70236,"count":2},{"bucket":70418,"count":2},{"bucket":70528,"count":2},{"bucket":70564,"count":2},{"bucket":70684,"count":2},{"bucket":70930,"count":2},{"bucket":71018,"count":2},{"bucket":71149,"count":2},{"bucket":71331,"count":2},{"bucket":71913,"count":2},{"bucket":72092,"count":2},{"bucket":72135,"count":2},{"bucket":72171,"count":2},{"bucket":72391,"count":2},{"bucket":72464,"count":2},{"bucket":72591,"count":2},{"bucket":72683,"count":2},{"bucket":72719,"count":2},{"bucket":72759,"count":2},{"bucket":73030,"count":2},{"bucket":73051,"count":2},{"bucket":73185,"count":2},{"bucket":73218,"count":2},{"bucket":73219,"count":2},{"bucket":73257,"count":2},{"bucket":73270,"count":2},{"bucket":73279,"count":2},{"bucket":73315,"count":2},{"bucket":73318,"count":2},{"bucket":73325,"count":2},{"bucket":73329,"count":2},{"bucket":73330,"count":2},{"bucket":73334,"count":2},{"bucket":73339,"count":2},{"bucket":73350,"count":2},{"bucket":73352,"count":2},{"bucket":73362,"count":2},{"bucket":73363,"count":2},{"bucket":73364,"count":2},{"bucket":73369,"count":2},{"bucket":73371,"count":2},{"bucket":73372,"count":2},{"bucket":73374,"count":2},{"bucket":73386,"count":2},{"bucket":73392,"count":2},{"bucket":73393,"count":2},{"bucket":73405,"count":2},{"bucket":73410,"count":2},{"bucket":73441,"count":2},{"bucket":73448,"count":2},{"bucket":73454,"count":2},{"bucket":73466,"count":2},{"bucket":73476,"count":2},{"bucket":73483,"count":2},{"bucket":73516,"count":2},{"bucket":73517,"count":2},{"bucket":73518,"count":2},{"bucket":73554,"count":2},{"bucket":73561,"count":2},{"bucket":73565,"count":2},{"bucket":73607,"count":2},{"bucket":73611,"count":2},{"bucket":-73048,"count":1},{"bucket":120530,"count":1},{"bucket":17443,"count":1},{"bucket":187625,"count":1},{"bucket":42916,"count":1},{"bucket":45034,"count":1},{"bucket":51681,"count":1},{"bucket":52010,"count":1},{"bucket":53909,"count":1},{"bucket":55334,"count":1},{"bucket":56064,"count":1},{"bucket":56174,"count":1},{"bucket":56612,"count":1},{"bucket":56722,"count":1},{"bucket":56893,"count":1},{"bucket":56904,"count":1},{"bucket":58000,"count":1},{"bucket":58292,"count":1},{"bucket":58402,"count":1},{"bucket":58438,"count":1},{"bucket":58575,"count":1},{"bucket":58584,"count":1},{"bucket":58652,"count":1},{"bucket":59680,"count":1},{"bucket":59972,"count":1},{"bucket":60045,"count":1},{"bucket":60484,"count":1},{"bucket":60849,"count":1},{"bucket":61251,"count":1},{"bucket":61616,"count":1},{"bucket":61908,"count":1},{"bucket":62091,"count":1},{"bucket":62146,"count":1},{"bucket":62200,"count":1},{"bucket":62639,"count":1},{"bucket":63150,"count":1},{"bucket":63588,"count":1},{"bucket":63807,"count":1},{"bucket":64063,"count":1},{"bucket":64136,"count":1},{"bucket":64142,"count":1},{"bucket":64209,"count":1},{"bucket":64319,"count":1},{"bucket":64611,"count":1},{"bucket":64867,"count":1},{"bucket":65305,"count":1},{"bucket":65451,"count":1},{"bucket":65519,"count":1},{"bucket":65999,"count":1},{"bucket":66255,"count":1},{"bucket":66328,"count":1},{"bucket":66364,"count":1},{"bucket":66401,"count":1},{"bucket":66437,"count":1},{"bucket":66547,"count":1},{"bucket":66620,"count":1},{"bucket":66839,"count":1},{"bucket":67204,"count":1},{"bucket":67241,"count":1},{"bucket":67387,"count":1},{"bucket":67423,"count":1},{"bucket":67621,"count":1},{"bucket":67642,"count":1},{"bucket":67752,"count":1},{"bucket":67935,"count":1},{"bucket":67971,"count":1},{"bucket":68044,"count":1},{"bucket":68117,"count":1},{"bucket":68227,"count":1},{"bucket":68263,"count":1},{"bucket":68300,"count":1},{"bucket":68410,"count":1},{"bucket":68592,"count":1},{"bucket":68612,"count":1},{"bucket":68629,"count":1},{"bucket":68665,"count":1},{"bucket":68735,"count":1},{"bucket":68740,"count":1},{"bucket":68773,"count":1},{"bucket":68789,"count":1},{"bucket":68954,"count":1},{"bucket":69067,"count":1},{"bucket":69068,"count":1},{"bucket":69169,"count":1},{"bucket":69294,"count":1},{"bucket":69313,"count":1},{"bucket":69323,"count":1},{"bucket":69464,"count":1},{"bucket":69703,"count":1},{"bucket":69781,"count":1},{"bucket":69797,"count":1},{"bucket":69870,"count":1},{"bucket":69908,"count":1},{"bucket":69936,"count":1},{"bucket":69980,"count":1},{"bucket":70004,"count":1},{"bucket":70090,"count":1},{"bucket":70272,"count":1},{"bucket":70273,"count":1},{"bucket":70345,"count":1},{"bucket":70455,"count":1},{"bucket":70457,"count":1},{"bucket":70462,"count":1},{"bucket":70470,"count":1},{"bucket":70660,"count":1},{"bucket":70685,"count":1},{"bucket":70710,"count":1},{"bucket":70719,"count":1},{"bucket":70721,"count":1},{"bucket":70766,"count":1},{"bucket":70861,"count":1},{"bucket":70871,"count":1},{"bucket":70923,"count":1},{"bucket":70966,"count":1},{"bucket":70983,"count":1},{"bucket":70984,"count":1},{"bucket":71012,"count":1},{"bucket":71076,"count":1},{"bucket":71112,"count":1},{"bucket":71218,"count":1},{"bucket":71234,"count":1},{"bucket":71306,"count":1},{"bucket":71390,"count":1},{"bucket":71446,"count":1},{"bucket":71563,"count":1},{"bucket":71569,"count":1},{"bucket":71575,"count":1},{"bucket":71581,"count":1},{"bucket":71590,"count":1},{"bucket":71629,"count":1},{"bucket":71718,"count":1},{"bucket":71721,"count":1},{"bucket":71723,"count":1},{"bucket":71729,"count":1},{"bucket":71748,"count":1},{"bucket":71753,"count":1},{"bucket":71782,"count":1},{"bucket":71785,"count":1},{"bucket":71803,"count":1},{"bucket":71840,"count":1},{"bucket":71855,"count":1},{"bucket":71865,"count":1},{"bucket":71943,"count":1},{"bucket":72036,"count":1},{"bucket":72062,"count":1},{"bucket":72094,"count":1},{"bucket":72110,"count":1},{"bucket":72165,"count":1},{"bucket":72201,"count":1},{"bucket":72203,"count":1},{"bucket":72225,"count":1},{"bucket":72250,"count":1},{"bucket":72260,"count":1},{"bucket":72261,"count":1},{"bucket":72270,"count":1},{"bucket":72298,"count":1},{"bucket":72332,"count":1},{"bucket":72365,"count":1},{"bucket":72388,"count":1},{"bucket":72412,"count":1},{"bucket":72425,"count":1},{"bucket":72437,"count":1},{"bucket":72519,"count":1},{"bucket":72538,"count":1},{"bucket":72564,"count":1},{"bucket":72601,"count":1},{"bucket":72613,"count":1},{"bucket":72619,"count":1},{"bucket":72637,"count":1},{"bucket":72680,"count":1},{"bucket":72687,"count":1},{"bucket":72708,"count":1},{"bucket":72709,"count":1},{"bucket":72715,"count":1},{"bucket":72724,"count":1},{"bucket":72736,"count":1},{"bucket":72747,"count":1},{"bucket":72761,"count":1},{"bucket":72764,"count":1},{"bucket":72799,"count":1},{"bucket":72882,"count":1},{"bucket":72884,"count":1},{"bucket":72901,"count":1},{"bucket":72906,"count":1},{"bucket":72921,"count":1},{"bucket":72924,"count":1},{"bucket":72927,"count":1},{"bucket":72929,"count":1},{"bucket":72933,"count":1},{"bucket":72969,"count":1},{"bucket":72970,"count":1},{"bucket":73003,"count":1},{"bucket":73015,"count":1},{"bucket":73023,"count":1},{"bucket":73055,"count":1},{"bucket":73061,"count":1},{"bucket":73082,"count":1},{"bucket":73101,"count":1},{"bucket":73136,"count":1},{"bucket":73140,"count":1},{"bucket":73147,"count":1},{"bucket":73154,"count":1},{"bucket":73160,"count":1},{"bucket":73197,"count":1},{"bucket":73199,"count":1},{"bucket":73207,"count":1},{"bucket":73230,"count":1},{"bucket":73234,"count":1},{"bucket":73237,"count":1},{"bucket":73242,"count":1},{"bucket":73246,"count":1},{"bucket":73260,"count":1},{"bucket":73261,"count":1},{"bucket":73268,"count":1},{"bucket":73269,"count":1},{"bucket":73272,"count":1},{"bucket":73274,"count":1},{"bucket":73283,"count":1},{"bucket":73288,"count":1},{"bucket":73301,"count":1},{"bucket":73302,"count":1},{"bucket":73303,"count":1},{"bucket":73308,"count":1},{"bucket":73309,"count":1},{"bucket":73314,"count":1},{"bucket":73331,"count":1},{"bucket":73333,"count":1},{"bucket":73336,"count":1},{"bucket":73341,"count":1},{"bucket":73343,"count":1},{"bucket":73346,"count":1},{"bucket":73355,"count":1},{"bucket":73365,"count":1},{"bucket":73375,"count":1},{"bucket":73379,"count":1},{"bucket":73382,"count":1},{"bucket":73388,"count":1},{"bucket":73389,"count":1},{"bucket":73395,"count":1},{"bucket":73396,"count":1},{"bucket":73397,"count":1},{"bucket":73398,"count":1},{"bucket":73403,"count":1},{"bucket":73407,"count":1},{"bucket":73415,"count":1},{"bucket":73417,"count":1},{"bucket":73421,"count":1},{"bucket":73424,"count":1},{"bucket":73425,"count":1},{"bucket":73426,"count":1},{"bucket":73434,"count":1},{"bucket":73437,"count":1},{"bucket":73444,"count":1},{"bucket":73445,"count":1},{"bucket":73446,"count":1},{"bucket":73447,"count":1},{"bucket":73452,"count":1},{"bucket":73457,"count":1},{"bucket":73462,"count":1},{"bucket":73467,"count":1},{"bucket":73470,"count":1},{"bucket":73472,"count":1},{"bucket":73473,"count":1},{"bucket":73482,"count":1},{"bucket":73488,"count":1},{"bucket":73489,"count":1},{"bucket":73492,"count":1},{"bucket":73493,"count":1},{"bucket":73496,"count":1},{"bucket":73502,"count":1},{"bucket":73505,"count":1},{"bucket":73506,"count":1},{"bucket":73508,"count":1},{"bucket":73509,"count":1},{"bucket":73511,"count":1},{"bucket":73514,"count":1},{"bucket":73520,"count":1},{"bucket":73522,"count":1},{"bucket":73529,"count":1},{"bucket":73534,"count":1},{"bucket":73536,"count":1},{"bucket":73540,"count":1},{"bucket":73541,"count":1},{"bucket":73545,"count":1},{"bucket":73553,"count":1},{"bucket":73558,"count":1},{"bucket":73564,"count":1},{"bucket":73576,"count":1},{"bucket":73577,"count":1},{"bucket":73578,"count":1},{"bucket":73583,"count":1},{"bucket":73593,"count":1},{"bucket":73597,"count":1},{"bucket":73613,"count":1},{"bucket":73614,"count":1},{"bucket":73616,"count":1},{"bucket":73619,"count":1},{"bucket":73625,"count":1},{"bucket":73626,"count":1}]});
			}, responseTimeBucket);
		}
    };

    data.prototype.getPhrases = function(term, scale, bucket, lastKey, callback) {
        if(!lastKey){
			setTimeout(function(){
				callback({"phraseList":[{"text":"Les premières découvertes paléontologiques situaient les premiers foyers de domestication du <B>chat</B> en Égypte,<strong> vers 2000 av. J.-C.</strong>, mais la découverte en 2004 par une équipe du CNRS dirigée par Jean Guilaine des restes d’un <B>chat</B> aux côtés de ceux d’un humain dans une sépulture à Chypre repousse le début de cette relation (processus biocénotique de commensalisme) entre 7 500 à 7 000 ans av. J.-C.","date":-730485,"pageUrl":"/Histoire_du_chat"},{"text":"Timothée II, dit Timothée Élure (c'est-à-dire en grec Αἴλουρος, « le <B>Chat</B> »), fut patriarche d'Alexandrie des monophysites de mars 457 à janvier 460 et de 475 à jusqu'à sa mort le<strong> 31 juillet 477</strong>","date":174433,"pageUrl":"/Timoth%C3%A9e_II_d%27Alexandrie"},{"text":"Les chats arrivent au Japon au VIe siècle en même temps que la doctrine bouddhiste, mais sa réelle introduction date du<strong> 19 septembre 999</strong>, date de l'anniversaire de l'empereur Ichijo, qui reçut un <B>chat</B> pour ses treize ans.","date":365139,"pageUrl":"/Histoire_du_chat"},{"text":"Les chats arrivent au Japon au VIe siècle en même temps que la doctrine bouddhiste, mais sa réelle introduction date du<strong> 19 septembre 999</strong>, date de l'anniversaire de l'empereur Ichijo, qui reçut un <B>chat</B> pour ses treize ans.","date":365139,"pageUrl":"/Chat-vampire_de_Nabeshima"},{"text":"C'est<strong> en 1175 </strong>que le mot <B>chat</B> apparait pour la première fois dans la langue française.","date":429160,"pageUrl":"/Histoire_du_chat"},{"text":"<strong>En 1233, </strong>la bulle Vox in rama (en) du pape Grégoire IX, créateur de l'Inquisition médiévale, considère que le <B>chat</B>, comme le crapaud, est une incarnation du Diable  et déclare que toute personne abritant un <B>chat</B> noir risque le bucher.","date":450345,"pageUrl":"/Chat_d%27argent"},{"text":"\"Comment attraper un poisson-<B>chat</B> avec une calebasse\" (瓢鮎図, Hyōnen zu,<strong> environ 1415, </strong>Taizō-in (退蔵院), Myôshin-ji (妙心寺), Kyôto), par le moine-peintre Josetsu (如拙), marque un tournant dans la peinture du Muromachi, caractérisé par une reconnaissance de l'importance du paysage.","date":516818,"pageUrl":"/Art_japonais"}],"lastKey":"f2924810-f2d0-4f49-9905-89f68737f4a5","total":1120});

			},responseTimePhrases);
		}else if(lastKey === "f2924810-f2d0-4f49-9905-89f68737f4a5"){
			setTimeout(function(){
			callback({"phraseList":[{"text":"Yi Su-mun, parti au Japon<strong> en 1424 </strong>pour échapper aux persécutions des bouddhistes, peint le tableau bien connu, « Prise d'un poisson-<B>chat</B> avec une gourde ».","date":520105,"pageUrl":"/Influence_cor%C3%A9enne_sur_la_culture_japonaise"},{"text":"Barthélemy d'Andlau (mort au château du Hugstein<strong> en 1476 </strong>étranglé dit-on par un <B>chat</B> noir)","date":539098,"pageUrl":"/Murbach"},{"text":"Le château de Landonvillers est une ancienne maison seigneuriale du<strong> XVIe siècle</strong> construite par Thomas du <B>Chat</B>.","date":547864,"pageUrl":"/Landonvillers"},{"text":"Ajouté<strong> en 1500 </strong>(par l'éditeur Bernard du Mont du <B>Chat</B>), en 1604 (par l'éditeur Oudot) et en 1703 (par l'éditeur Le Dispensateur des secrets).","date":547864,"pageUrl":"/Grand_Albert"},{"text":"Elle expose au Salon à partir de 1876: en 1876 (Portrait de l'auteur); en 1879 (Bourgeoise de Nuremberg, au<strong> XVIe siècle</strong>); en 1885 (Le jour des Rameaux); en 1904, une étude (Le <B>chat</B> à la guêpe).","date":547864,"pageUrl":"/%C3%89tienne_Leroy"},{"text":"Son fils Artus Gouffier de Boissy, gouverneur de François Ier, est nommé grand-maître de France<strong> en 1515 </strong>et duc de Roannez et pair de France; accompagnant Charles VIII et Louis XII en Italie où il reçut la terre de Caravaz – titre devenu marquisat de Carabas dans Le <B>Chat</B> Botté de Charles Perrault – il s'intéressa à l'art de ce pays et a pu véritablement commencer l'importante collection d'art familiale.","date":553342,"pageUrl":"/Ch%C3%A2teau_d%27Oiron"},{"text":"Il raconte sa randonnée dans son livre best-seller The Ride ou Tschiffely's Ride, il y décrit son voyage épique de trois ans, de 1925 à 1928, avec ses deux chevaux \"Mancha\" (\"Tacheté\") et \"Gato\" (\"<B>Chat</B>\") natifs d'Argentine, de race Criollo, descendants directs des chevaux amenés par le conquistador Pedro de Mendoza<strong> en 1535 </strong>(les premiers chevaux amenés dans le Nouveau-Monde).","date":560647,"pageUrl":"/Aim%C3%A9_F%C3%A9lix_Tschiffely"}],"lastKey":"e3d7a8ca-eb06-4111-9b79-cb75a61b36d7","total":1120});
			},responseTimePhrases);
		}else if(lastKey === "e3d7a8ca-eb06-4111-9b79-cb75a61b36d7"){
				setTimeout(function(){

			callback({"phraseList":[{"text":"Stefano Felis (né<strong> vers 1538, </strong>mort le 25 septembre 1603), forme latinisée de son nom de famille Gatto («<B>chat</B>», en français), est un compositeur italien de la Renaissance, établi à Naples, et le collaborateur et professeur probable du compositeur Pomponio Nenna.","date":561743,"pageUrl":"/Stefano_Felis"},{"text":"<strong>En 1550 </strong>déjà, Peder Claussøn Friis, un pasteur passionné de la faune et la flore de son pays avait classifié le « lynx norvégien » en trois catégories : le loup-lynx, le renard-lynx et le <B>chat</B>-lynx.","date":566126,"pageUrl":"/Norv%C3%A9gien_(chat)"},{"text":"<strong>En 1553, </strong>La Chatte de Constantin le fortuné (Costantino Fortunato), un conte similaire au <B>Chat</B> botté, est publié à Venise dans Les Nuits facétieuses (Le Piacevole notti) de Giovanni Francesco Straparola,.","date":567222,"pageUrl":"/Le_Ma%C3%AEtre_chat_ou_le_Chat_bott%C3%A9"},{"text":"Le fief et la maison de Malet, dépendance de Choisel, sont la possession, au XVIe siècle, de la famille noble des de La Portes, seigneur de Malet et Saint-Paul ; y habite, vers 1540, noble Claude de La Porte, seigneur de Malet, condamné à mort par décapitation à Chambéry, le<strong> 9 septembre 1557</strong> pour le meurtre de maître Nicolas Dapponex, arquebusé, avec la complicité de ses frères et de sa femme, lors de sa traversée du col du <B>Chat</B>.","date":568934,"pageUrl":"/Maison_noble_de_Malet"},{"text":"Le chartreux apparaît pour la première fois<strong> en 1558 </strong>dans un poème de Joachim du Bellay intitulé Vers Français sur la mort d'un petit <B>chat</B>.","date":569048,"pageUrl":"/Chartreux_(chat)"},{"text":"Par exemple,<strong> en 1581, </strong>Montaigne décrit le mont du <B>Chat</B>, le lac du Bourget et le château de Bourdeau :","date":577449,"pageUrl":"/Savoie_(d%C3%A9partement)"},{"text":"<strong>En 1581, </strong>passe le plus célèbre des touristes qui empruntera jamais le col du <B>Chat</B>, à savoir Michel de Montaigne de retour d'Italie :","date":577449,"pageUrl":"/Col_du_Chat"}],"total":1120});
		},responseTimePhrases);
    }};

    data.prototype.getSynonyms = function(term, callback) {
        callback(["minet","mimi","minon"]);
    };

    Time.Data = data;

})();