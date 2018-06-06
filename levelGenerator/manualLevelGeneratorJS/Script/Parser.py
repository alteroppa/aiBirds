import json
import re
import math
from slpp import slpp
from pprint import pprint

import os

def main():
	for filename in os.listdir('levelsToParse/'):
		if filename == ".DS_Store":
			continue

		print(filename)
		# put the path of the downloaded file here that you generated manually
		path = 'levelsToParse/' + filename
		# put the path of the outputfile here (you can also change the Levelname to generate Level1-2 (second level),..)
		pathjson = filename
		prefile = 'partjson.json'
		dictionaryPath = 'Dictionary.json'

		with open(path) as f:
			levellua = f.read()
		with open(dictionaryPath) as f:
			dictionary = json.load(f)


		worldstart = levellua.find('world = ')
		levellua = levellua[worldstart+8:]
		level = slpp.decode(levellua)

		number = 0
		numberBlock = 0
		x=0
		del level['ground']
		levelNew = dict()
		for key in level:

			if 'Bird' in key:
				number+=1
				levelNew['bird_'+str(number)]={}
				levelNew['bird_'+str(number)]['angle'] = 0
				levelNew['bird_'+str(number)]['x'] = x
				levelNew['bird_'+str(number)]['y'] = -1.142
				levelNew['bird_'+str(number)]['id'] = dictionary[level[key]['definition']]
				x-=3


			else:
				numberBlock+=1
				levelNew['block_'+str(numberBlock)]={}
				angle = (level[key]['angle'])*180/math.pi
				if angle < 0:
					angle = 360+angle
				levelNew['block_'+str(numberBlock)]['angle'] = angle
				levelNew['block_'+str(numberBlock)]['x'] = (level[key]['x']*0.91)+52
				levelNew['block_'+str(numberBlock)]['y'] = level[key]['y']*0.91
				levelNew['block_'+str(numberBlock)]['id'] = dictionary[level[key]['definition']]


		with open(prefile) as f:
			levelgeneral = json.load(f)

		levelgeneral['world']=levelNew
		levelgeneral['counts'] = {'birds': number, 'blocks': numberBlock}
		leveljson = json.dumps(levelgeneral)
		file = open('parsedLevels/' + pathjson, 'w') # here, you could enter your slingshot/fowl/cors/json folder, in order to inject parsed levels directly
		file.write(leveljson)
		file.close()
		pprint(levelgeneral)

if __name__ == '__main__':
    main()
   
						
					
		