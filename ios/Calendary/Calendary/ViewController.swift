//
//  ViewController.swift
//  Calendary
//
//  Created by 김기현 on 23/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FSCalendar

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, FSCalendarDelegate {
    
    @IBOutlet weak var editButton: UIButton!
    @IBOutlet weak var calendar: FSCalendar!
    @IBOutlet weak var listTableView: UITableView!
    
    var date: Date = Date()
    var selectedDateObserver: NSKeyValueObservation? = nil
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell =  listTableView.dequeueReusableCell(withIdentifier: "listCell", for: indexPath) as! ListTableViewCell
        
        cell.workLabel.text = "캘린더리"
        
        return cell
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        listTableView.delegate = self
        listTableView.dataSource = self
        
        calendar.delegate = self
        
        editButton.layer.cornerRadius = 35
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "Calendary.png")
        imageView.image = image
        navigationItem.titleView = imageView
        self.navigationController?.navigationBar.shadowImage = UIImage()
    }
    
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        
        if date > self.date {
            self.editButton.isHidden = true
        } else {
            self.editButton.isHidden = false
        }
        
    }
    
    deinit {
        selectedDateObserver?.invalidate()
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier == "addDiary" {
            let addDiaryViewContollrer = segue.destination as? AddDiaryViewController
            addDiaryViewContollrer?.date = calendar.selectedDate ?? Date()
            print(calendar.selectedDate)
        }
    }

}

