//
//  ViewController.swift
//  Calendary
//
//  Created by 김기현 on 23/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FSCalendar
import FirebaseAuth
import FirebaseFirestore

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, FSCalendarDelegate {
    
    @IBOutlet weak var editButton: UIButton!
    @IBOutlet weak var calendar: FSCalendar!
    @IBOutlet weak var listTableView: UITableView!
    @IBOutlet weak var addDiaryButton: UIButton!
    @IBOutlet weak var doWorkButton: UIButton!
    @IBOutlet weak var addButtonConstraint: NSLayoutConstraint!
    @IBOutlet weak var doWorkButtonConstraint: NSLayoutConstraint!
    
    let db = Firestore.firestore()
    let firebaseAuth = Auth.auth()
    
    var click: Bool = true
    var date: Date = Date()
    var diarys: [DiaryContent] = []
    var selectedDateObserver: NSKeyValueObservation? = nil
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return diarys.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell =  listTableView.dequeueReusableCell(withIdentifier: "listCell", for: indexPath) as! ListTableViewCell
        
        cell.workLabel.text = diarys[indexPath.row].title
        
        return cell
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    func getDocumentFromFirebase() {
        let selectedDate = calendar.selectedDate ?? Date()
        let selectedDatePlusDay = Calendar.current.date(byAdding: .day, value: 1, to: selectedDate) ?? Date()
        db.collection("Content").whereField("select timestamp", isGreaterThanOrEqualTo: selectedDate).whereField("select timestamp", isLessThan: selectedDatePlusDay).getDocuments { (querySnapshot, error) in
            if let error = error {
                print("Error getting document: \(error)")
            } else {
                self.diarys = []
                for document in querySnapshot!.documents {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, timestamp: (document.get("timestamp") as! Timestamp).dateValue(), selectTimestamp: (document.get("select timestamp") as! Timestamp).dateValue(), show: (document.get("show") as? String) ?? "", userId: document.get("user id") as! String)
                    self.diarys.append(diaryContent)
                }
                self.listTableView.reloadData()
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        listTableView.delegate = self
        listTableView.dataSource = self
        
        calendar.delegate = self
        
        editButton.layer.cornerRadius = 25
        addDiaryButton.layer.cornerRadius = 25
        doWorkButton.layer.cornerRadius = 25
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "Calendary.png")
        imageView.image = image
        navigationItem.titleView = imageView
        self.navigationController?.navigationBar.shadowImage = UIImage()
    }
    
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        
        getDocumentFromFirebase()
        
        if date > self.date {
            self.editButton.isHidden = true
        } else {
            self.editButton.isHidden = false
        }
        
    }
    
    @IBAction func plusButtonClick(_ sender: Any) {

        if click == true {
            addDiaryButton.isHidden = false
            doWorkButton.isHidden = false
            UIView.animate(withDuration: 0.5) {
                self.addButtonConstraint.constant = 76
                self.doWorkButtonConstraint.constant = 136
                self.view.layoutIfNeeded()
            }
        
            click = false
        } else {
            UIView.animate(withDuration: 0.5, animations: {
                self.addButtonConstraint.constant = 16
                self.doWorkButtonConstraint.constant = 16
                self.view.layoutIfNeeded()
            }) { _ in
                self.addDiaryButton.isHidden = true
                self.doWorkButton.isHidden = true
            }
            
            click = true
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

