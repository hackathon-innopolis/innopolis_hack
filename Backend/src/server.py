from flask import Flask, request, send_file
import sqlite3 as sql
import json
from io import StringIO
from PIL import Image
app = Flask(__name__)
counter = 0


@app.route("/fetch", methods = ['POST'])
def make_someones_dream_come_true():
    with sql.connect("./database/main.db") as conn:
        name = request.form['name']
        xcoord = float(request.form['x'])
        ycoord = float(request.form['y'])
        c = conn.cursor()
        c.execute("SELECT * FROM wishes WHERE (x BETWEEN {} AND {}) AND (y BETWEEN {} AND {})".format(xcoord - 0.005, xcoord + 0.005, ycoord - 0.008, ycoord + 0.008))
        return json.dumps(c.fetchall())

@app.route("/add", methods = ['POST'])
def add_a_wish():
    with sql.connect("./database/main.db") as conn:
        name = request.form['name']
        xcoord = request.form['x']
        ycoord = request.form['y']
        wish = request.form['wish']
        c = conn.cursor()
        if bal(name) > 0:
            c.execute("SELECT ROWID FROM wishes")
            id = c.fetchall()[-1][0]
            c.execute("INSERT INTO wishes (id, name, x, y, wish) VALUES ({}, \"{}\", {}, {}, \"{}\")".format(id + 1, name, xcoord, ycoord, wish))
            c.execute("UPDATE balance SET bal = bal - 1 WHERE name = \"{}\"".format(name))
            return "OK"
        else:
            return "You don't have enough money"

@app.route("/take", methods = ['POST'])
def take_someones_dream():
    with sql.connect("./database/main.db") as conn:
        name = request.form['name']
        xcoord = request.form['x']
        ycoord = request.form['y']
        wish_id = request.form['wish-id']
        c = conn.cursor()
        if wish_id == 0:
            return "Error"
        else:
            c.execute("DELETE FROM wishes WHERE id = {}".format(wish_id))
            c.execute("UPDATE balance SET bal = bal + 1 WHERE name = \"{}\"".format(name))
            return "OK"

@app.route("/balance", methods = ['POST'])
def get_balance():
    name = request.form['name']
    return str(bal(name))

@app.route("/buy", methods=['POST'])
def buy_a_little_bit():
    with sql.connect("./database/main.db") as conn:
        name = request.form['name']
        count = request.form['count']
        c = conn.cursor()
        c.execute("UPDATE balance SET bal = bal + {} WHERE name \"{}\"".format(count, name))
        return "OK"

@app.route("/map", methods = ['POST'])
def get_map():
    xcoord = request.form['x']
    ycoord = request.form['y']
    return make_a_map([float(xcoord), float(ycoord)])

def bal(name):
    with sql.connect("./database/main.db") as conn:
        c = conn.cursor()
        c.execute("SELECT name FROM balance")
        for i in c.fetchall():
            if i[0] == name:
                c.execute("SELECT bal FROM balance WHERE name = '{}'".format(name))
                bal = c.fetchone()[0]
                return int(bal)
        c.execute("INSERT INTO balance (name, bal) VALUES ('{}', {})".format(name, 1))
        conn.commit()
        return 1

def convert(coordniate):
    coord = [0, 0]
    coord[0] = int((coordniate[1] - 60.5) / 0.2 * 8506)
    coord[1] = int((coordniate[0] - 56.8) / 0.1 * 9322)
    return coord

def make_a_map(coordinate):
    global counter
    coordinate = convert(coordinate)
    print(coordinate)
    img = Image.open('../img/full_map.png')
    k = 350
    area = (coordinate[0] - k, coordinate[1] - k, coordinate[0] + k, coordinate[1] + k)
    new_img = img.crop(area)
    new_img.save("{}.png".format(counter))
    counter += 1
    return send_file('{}.png'.format(counter - 1), mimetype='image/png')


if __name__ == "__main__":
    app.run()
