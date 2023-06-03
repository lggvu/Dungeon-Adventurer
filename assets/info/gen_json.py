import json

dct = {
    "Gun 1": {
        'type': 'Gun',
        'gun_texture': 'weapon/weapon.png',
        'bullet_texture': 'bullet/bullet1.png'
    },
    'Gun 2': {
        'type': 'Gun',
        'gun_texture': 'weapon/weapon1.png',
        'bullet_texture': 'bullet/bullet3.png'
    },
    "Sword 1": {
        "type": "Sword",
        "sword_texture": "weapon/sword.png",
        "effect_texture": "weapon/sword-effect-test.png"
    }
}

json_object = json.dumps(dct, indent = 4) 

with open('weapon_info.json', 'w') as f:
    f.write(json_object)