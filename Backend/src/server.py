from flask import Flask, request
import sqlite3 as sql
import json
app = Flask(__name__)

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

@app.route("/map", methods = ['POST'])
def get_map():
    xcoord = request.form['x']
    ycoord = request.form['y']

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

if __name__ == "__main__":
    app.run()
