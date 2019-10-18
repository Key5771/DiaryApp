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
    @IBOutlet var popOverView: UIView!
    @IBOutlet weak var popOverDateLabel: UILabel!
    @IBOutlet weak var doWorkContent: UITextField!
    @IBOutlet weak var doWorkSaveButton: UIButton!
    @IBOutlet weak var popOverDateLabel2: UILabel!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    let db = Firestore.firestore()
    let firebaseAuth = Auth.auth()
    
    var click: Bool = true
    var date: Date = Date()
    var dowork: [DoworkContent] = []
    var diarys: [DiaryContent] = []
    var selectedDateObserver: NSKeyValueObservation? = nil
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return dowork.count
        } else if section == 1 {
            return diarys.count
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell =  listTableView.dequeueReusableCell(withIdentifier: "listCell", for: indexPath) as! ListTableViewCell
        if indexPath.section == 0 {
//            cell.workLabel.text = dowork[indexPath.row]
        } else if indexPath.section == 1 {
            cell.workLabel.text = diarys[indexPath.row].title
        }
        
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
                for document in querySnapshot!.documents where document.get("user id") as? String == self.firebaseAuth.currentUser?.email {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, timestamp: (document.get("timestamp") as! Timestamp).dateValue(), selectTimestamp: (document.get("select timestamp") as! Timestamp).dateValue(), show: (document.get("show") as? String) ?? "", userId: document.get("user id") as! String)
                    self.diarys.append(diaryContent)
                }
                self.listTableView.reloadData()
            }
        }
    }
    
    func getDoworkFromFirebase() {
//        let selectedDate
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
        
        popOverView.layer.borderColor = UIColor(displayP3Red: 0/255, green: 0/255, blue: 0/255, alpha: 1).cgColor
        popOverView.layer.borderWidth = 1
        
    }
    
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        
        getDocumentFromFirebase()
        
//        if date > self.date {
//            self.editButton.isHidden = true
//        } else {
//            self.editButton.isHidden = false
//        }
        
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
    
    
    @IBAction func doWorkButtonClick(_ sender: Any) {
        self.view.addSubview(popOverView)
        popOverView.center = self.view.center
        
        let dateFormat: DateFormatter = DateFormatter()
        dateFormat.dateFormat = "yyyy년 MM월 dd일"
        if calendar.selectedDate == nil {
            let selectDate: String = dateFormat.string(from: date)
            popOverDateLabel.text = selectDate as? String
        } else {
            let selectDate: String = dateFormat.string(from: self.calendar.selectedDate!)
            popOverDateLabel.text = selectDate as? String
        }
        
        popOverDateLabel2.text = " 의 할일"
        
        
    }
    
    @IBAction func doWorkSave(_ sender: Any) {
        doWorkContent.resignFirstResponder()
        
        activityIndicator.startAnimating()
        
        doWorkSaveButton.isEnabled = false
        
        var ref: DocumentReference? = nil
        
        ref = db.collection("Todo").addDocument(data: [
            "timestamp": date,
            "todo": doWorkContent.text ?? "",
            "user id": firebaseAuth.currentUser?.email
        ]) { err in
            self.activityIndicator.stopAnimating()
            var alertTitle = "저장되었습니다."
            var alertMessage = "성공적으로 저장되었습니다."
            if err != nil {
                alertTitle = "실패하였습니다."
                alertMessage = "저장에 실패하였습니다."
            }
            
            let alertController = UIAlertController(title: alertTitle, message: alertMessage, preferredStyle: .alert)
            let okButton = UIAlertAction(title: "확인", style: .default, handler: {
                (_) in
                self.popOverView.removeFromSuperview()
            })
            alertController.addAction(okButton)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    
    @IBAction func doWorkCancel(_ sender: Any) {
        self.popOverView.removeFromSuperview()
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

