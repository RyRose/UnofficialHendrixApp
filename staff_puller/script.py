#!/usr/bin/python3
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from lxml import etree
from datetime import datetime

import logging
import subprocess
import time

class DatabasePuller:
    def __init__(self):
        self.driver = webdriver.PhantomJS(executable_path="/usr/local/bin/phantomjs")
        self.base_url = "https://hendrix.edu/Directory/"
        self.button_id = "ctl00_ctl00_BodyContent_MiddleMainContentPlaceHolder_EmployeeDirectorySearcher1_BeginSearchButton"
        self.table_id = "ctl00_ctl00_BodyContent_MiddleMainContentPlaceHolder_EmployeeDirectoryViewer1_EmployeeViewerResultsGrid"

    def initialize(self):
        self.driver.set_page_load_timeout(10)
        self.driver.get(self.base_url)
        WebDriverWait( self.driver, 20).until(EC.element_to_be_clickable( (By.ID, self.button_id) ) )
        self.driver.find_element_by_id(self.button_id).click()

    def get_number_of_staff(self):
        WebDriverWait(self.driver, 20).until(EC.presence_of_element_located( (By.ID, self.table_id) ))
        blank_results_found = self.driver.find_element_by_css_selector(".EmployeeDirectoryViewerContainer > span").text
        num_staff = int( blank_results_found.split(" ")[0] )
        return num_staff + 2

    def go_to_staff(self, staff_index):
        if (staff_index < 10):
            staff_index = '0' + str(staff_index)
        person_selector = "table td a[href*='EmployeeViewerResultsGrid$ctl" + str(staff_index) + "']"
        WebDriverWait(self.driver, 20).until(EC.element_to_be_clickable( (By.CSS_SELECTOR, person_selector) ))
        self.driver.find_element_by_css_selector(person_selector).click()

    def return_to_table(self):
        WebDriverWait(self.driver, 20).until( EC.element_to_be_clickable( (By.ID, self.button_id) ) )
        self.driver.find_element_by_id(self.button_id).click()

    def get_item(self):
        WebDriverWait(self.driver, 20).until(EC.presence_of_element_located( (By.CLASS_NAME, "DirectoryPerson")))
        driver = self.driver
        name = driver.find_element_by_css_selector(".DirectoryDisplayName").text
        title = driver.find_element_by_css_selector(".DirectoryTitle span:first-child").text
        pic = driver.find_element_by_css_selector(".DirectoryPicture img").get_attribute("src")   
        pic = pic.split("?")[0]  # gets rid of sizing e.g. removes ?w=150&h=192
        department = driver.find_element_by_css_selector(".DirectoryTitle span:last-child").text
        email = driver.find_element_by_css_selector(".DirectoryEmail a").text
        phone = driver.find_element_by_css_selector(".DirectoryEmail span").text
        location_line_1 = driver.find_element_by_css_selector(".DirectoryLocationLine1 span").text
        location_line_2 = driver.find_element_by_css_selector(".DirectoryLocationLine2 span").text

        item_list = [ ("pic", pic), ("name", name), ("title", title), ("department", department),
        ("phone", phone), ("email", email), ("location_line1", location_line_1), 
        ("location_line2", location_line_2) ]

        item = etree.Element("item")

        for node in item_list:
            xml_node = etree.Element(node[0])
            xml_node.text = node[1].strip()
            item.append(xml_node)

        return item

    def reset(self):
        self.driver.quit()
        self.driver = webdriver.PhantomJS(executable_path="/usr/local/bin/phantomjs")

        self.initialize()

    def quit(self):
        self.driver.quit()

def main():
    puller = DatabasePuller()
    try: 
        puller.initialize()
        root = etree.Element('root')
        num_staff = puller.get_number_of_staff()
        logging.info("Acquired number of staff members: " + str(num_staff - 2))
        for i in range(2, num_staff):
            handle_advance_to_staff(puller, i)
            root.append(puller.get_item())
            handle_return_to_table(puller, i)
        file = open("out/staff.xml", "wb")
        file.write(etree.tostring(root, encoding="utf-8"))
        file.close()
        logging.info("Database pull succeeded!")
    finally:
        puller.quit()

def handle_advance_to_staff(database_puller, staff_index):
    i = 0
    while True:
        i += 1
        try:
            database_puller.go_to_staff(staff_index)
            break
        except Exception:
            logging.warning("Attempt: %s. Failed at going to staff at %s. Re-initializing.", i, staff_index)
            handle_reset(database_puller)

def handle_return_to_table(database_puller, staff_index):
    try:
        database_puller.return_to_table()
    except Exception:
        logging.warning("Failed at coming from staff at %s. Re-initializing.", staff_index)
        handle_reset(database_puller)

def handle_reset(database_puller):
    i = 0
    while True:
        i += 1
        try:
            database_puller.reset()
            break
        except Exception:
            logging.warning("Failed at initialization. Sleep for %s seconds.", i * 10)
            time.sleep(10*i)

if __name__ == "__main__":
    logging.basicConfig(format='%(levelname)s:%(message)s', filename="out/script.log", level=logging.INFO)
    logging.info(datetime.today())
    try:
        main()
    except Exception as e:
        logging.warning("Database pull failed!")
        logging.exception(e)
        subprocess.call("mail_error.sh")
    finally:
        logging.info(datetime.today())
        file = open("out/script.log", "a")
        file.write("----------\n")
        file.close()
