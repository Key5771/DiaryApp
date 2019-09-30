//
//  DiaryViewController.swift
//  Calendary
//
//  Created by 김기현 on 28/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore
import FirebaseAuth

class DiaryViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    var diarys: [DiaryContent] = [] {
        didSet {
            dataFiltered()
        }
    }
    var filteredDiarys: [DiaryContent] = []

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var editButton: UIBarButtonItem!
    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    @IBOutlet weak var segment: UISegmentedControl!
    
    private let refreshControl = UIRefreshControl()
    
    let db = Firestore.firestore()
    let firebaseAuth = Auth.auth()
    
    enum FilterMethod {
        case newWrite
        case oldWrite
        case newSelectTimestamp
        case oldSelectTimestamp
        case doWork
    }
    
    var method: FilterMethod = .doWork {
        didSet {
            dataFiltered()
        }
    }
    
    @IBAction func selectSegment(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            method = .newWrite
        } else if sender.selectedSegmentIndex == 1 {
            method = .newSelectTimestamp
        } else if sender.selectedSegmentIndex == 2 {
            method = .doWork
        }
    }
    
    
    func dataFiltered() {
        switch method {
        case .newWrite:
            filteredDiarys = diarys.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.timestamp > rhs.timestamp
            })
        case .oldWrite:
            filteredDiarys = diarys.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.timestamp < rhs.timestamp
            })
        case .newSelectTimestamp:
            filteredDiarys = diarys.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.selectTimestamp > rhs.selectTimestamp
            })
        case .oldSelectTimestamp:
            filteredDiarys = diarys.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.selectTimestamp < rhs.selectTimestamp
            })
        case .doWork:
            filteredDiarys = diarys
        @unknown default:
            filteredDiarys = diarys
        }
        tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return filteredDiarys.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "diaryCell", for: indexPath) as! DiaryTableViewCell
        cell.titleLabel.text = filteredDiarys[indexPath.row].title
        cell.contentLabel.text = filteredDiarys[indexPath.row].content
        
        let dateFormat: DateFormatter = DateFormatter()
        dateFormat.dateFormat = "yyyy년 MM월 dd일"
        cell.dateLabel.text = dateFormat.string(from: filteredDiarys[indexPath.row].selectTimestamp)
        return cell
    }

    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let diary = self.diarys.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
            db.collection("Content")
                .document(diary.id)
                .collection("Favorite")
                .getDocuments { (snapshot, error) in
                snapshot?.documents.forEach { $0.reference.delete() }
            }
            db.collection("Content")
                .document(diary.id)
                .collection("Comment")
                .getDocuments { (snapShot, error) in
                    snapShot?.documents.forEach { $0.reference.delete() }
            }
            db.collection("Content").document(diary.id).delete() { err in
                if let err = err {
                    print("Error removing document: \(err)")
                    self.diarys.insert(diary, at: indexPath.row)
                    tableView.insertRows(at: [indexPath], with: .automatic)
                } else {
                    print("Document successfully removed!")
                }
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        getDocumentFromNetwork()
    }
    
    func getDocumentFromNetwork() {
        
        db.collection("Content").whereField("user id", isEqualTo: firebaseAuth.currentUser?.email).getDocuments(completion:  { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                self.activityIndicatorView.stopAnimating()
                self.diarys = []
                
                for document in querySnapshot!.documents {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, timestamp: (document.get("timestamp") as! Timestamp).dateValue(), selectTimestamp: (document.get("select timestamp") as! Timestamp).dateValue(), show: (document.get("show") as? String) ?? "", userId: document.get("user id") as! String)
                    self.diarys.append(diaryContent)
                }
                self.refreshControl.endRefreshing()
                self.tableView.reloadData()
            }
        })
    }
    
    
    @IBAction func edit(_ sender: Any) {
        if tableView.isEditing {
            tableView.setEditing(false, animated: true)
            editButton.title = "편집"
        } else {
            tableView.setEditing(true, animated: true)
            editButton.title = "완료"
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        activityIndicatorView.startAnimating()

//        db.collection("Content").getDocuments(source: .cache) { (querySnapshot, err) in
//            if let err = err {
//                print("Error getting documents: \(err)")
//            } else {
//                self.diarys = []
//                for document in querySnapshot!.documents {
//                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, timestamp: (document.get("timestamp") as! Timestamp).dateValue(), selectTimestamp: (document.get("select timestamp") as! Timestamp).dateValue(), show: (document.get("show") as? String) ?? "", userId: document.get("user id") as! String)
//                    self.diarys.append(diaryContent)
//                }
//
//                self.tableView.reloadData()
//            }
//        }
        
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(refresh), for: .valueChanged)
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "Calendary.png")
        imageView.image = image
        navigationItem.titleView = imageView
        
        // Do any additional setup after loading the view.
    }
    
    @objc func refresh() {
        getDocumentFromNetwork()
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        
        if segue.identifier == "editDiary" {
            if let row = tableView.indexPathForSelectedRow {
                let vc = segue.destination as? ContentViewController
                vc?.modalPresentationStyle = .overFullScreen
                vc?.diaryId = diarys[row.row].id
                tableView.deselectRow(at: row, animated: true)
            }
        }
    }
 

}
