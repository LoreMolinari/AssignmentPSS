# user.py
import os
import datetime
import mysql.connector

# Estrapolo la password del database dalle variabili ambientali CI/CD di gitlab
db_password = os.environ.get("DB_PASSWORD")


class User:

    def __init__(self):
        # Configuro la connessione al database
        self.connection = mysql.connector.connect(
            host="mysql",
            user="root",
            password=db_password,
            database="testDB"
        )

    def increment(self):
        """Questa classe gestisce le operazioni
        legate all'incremento di accessi."""
        with self.connection.cursor() as cursor:
            current_time = datetime.datetime.now()
            cursor.execute(
                "INSERT INTO access_logs (timestamp) VALUES (%s)",
                (current_time,)
            )
            self.connection.commit()

    def get_count(self):
        """Questa classe effettua il conteggio di accessi."""
        with self.connection.cursor() as cursor:
            cursor.execute("SELECT COUNT(*) FROM access_logs")
            count = cursor.fetchone()[0]
        return count
