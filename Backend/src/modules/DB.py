import sqlite3
class database():
    def __init__(self):
        self.conn = sqlite3.connect('./database/main.db')
        self.c = self.conn.cursor()
