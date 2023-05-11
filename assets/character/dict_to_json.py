import json, os

dct = {
    'character/img1.png' : {
        'paddingWidth': 0,
        'paddingHeight': 0,
        'gapWidth': 0,
        'gapHeight': 0,
        'imgCharacterWidth': 48,
        'imgCharacterHeight': 72,
        'totalCols': 12,
        'totalRows': 8,
        'character': {
            'king': {
                'startCol': 0,
                'startRow': 0,
                'frameCols': 3,
                'frameRows': 4,
                'direction': {
                    270: 0,
                    180: 1,
                    0: 2,
                    90: 3,
                }
            },
            'princess': {
                'startCol': 6,
                'startRow': 0,
                'frameCols': 3,
                'frameRows': 4,
                'direction': {
                    270: 0,
                    180: 1,
                    0: 2,
                    90: 3,
                }
            }
        }
    }, 

    'character/img2.png' : {
        'paddingWidth': 0,
        'paddingHeight': 0,
        'gapWidth': 25,
        'gapHeight': 28,
        'imgCharacterWidth': 50,
        'imgCharacterHeight': 90,
        'totalCols': 8,
        'totalRows': 8,
        'character': {
            'young': {
                'startCol': 0,
                'startRow': 0,
                'frameCols': 8,
                'frameRows': 8,
                'direction': {
                    270: 0,
                    180: 1,
                    0: 2,
                    90: 3,
                    225: 4,
                    315: 5,
                    135: 6,
                    45: 7
                }
            }
        }
    }
}

current_folder = os.path.dirname(os.path.realpath(__file__))
with open(os.path.join(current_folder, "info.json"), "w") as outfile:
    outfile.write(json.dumps(dct, indent=4))