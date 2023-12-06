# user_test.py
import unittest
import mysql.connector
import os
from user import User

# Estrapolo la password del database dalle variabili ambientali CI/CD di gitlab
db_password = os.environ.get("MYSQL_ROOT_PASSWORD")

class TestUserClass(unittest.TestCase):

    def setUp(self):
        # Configuro il database di test
        self.connection = mysql.connector.connect(
            host="mysql",
            user="root",
            password=db_password,
        )

        # Per testare il corretto funzionamento devo creare il database per il service della pipeline di Gitlab
        self.create_database()

        # Mi connetto al database appena creato
        self.connection.database = "testDB"

        # Crea la tabella access_logs se non esiste già nel database
        self.create_access_logs_table()

    def tearDown(self):
        # Al momento della chiusura pulisco il database
        self.connection.close()

    def test_increment(self):
        # Eseguo i test relativi alla classe Utente con i relativi metodi
        user = User()
        user.increment()
        count = user.get_count()
        self.assertEqual(count, 1)

    def create_database(self):
        """Creo il database"""
        cursor = self.connection.cursor()
        if self.connection.is_connected():
            cursor.execute("CREATE DATABASE testDB")
            self.connection.commit()


    def create_access_logs_table(self):
        """Creo la tabella access_logs se non esiste"""
        with self.connection.cursor() as cursor:
            cursor.execute("CREATE TABLE IF NOT EXISTS access_logs (id INT AUTO_INCREMENT PRIMARY KEY, timestamp DATETIME NOT NULL)")
            self.connection.commit()

if __name__ == '__main__':
    unittest.main()
