//
//  DiaryViewController.swift
//  Calendary
//
//  Created by 김기현 on 28/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore

class DiaryViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    var diarys: [DiaryContent] = []
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var editButton: UIBarButtonItem!
    
    private let refreshControl = UIRefreshControl()
    
    let db = Firestore.firestore()
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return diarys.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "diaryCell", for: indexPath) as! DiaryTableViewCell
        cell.titleLabel.text = diarys[indexPath.row].title
        cell.contentLabel.text = diarys[indexPath.row].content
        cell.dateLabel.text = diarys[indexPath.row].date
        return cell
    }

    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let diary = self.diarys.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
            db.collection("diarys").document(diary.id).delete() { err in
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
        db.collection("diarys").getDocuments(source: .default) { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                self.diarys = []
                
                for document in querySnapshot!.documents {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, date: document.get("date") as! String)
                    self.diarys.append(diaryContent)
                }
                
//                for change in querySnapshot!.documentChanges {
//                    if change.type == .added {
//                        self.diarys.append(DiaryContent(id: change.document.documentID, title: change.document.get("title") as! String, content: change.document.get("content") as! String, date: change.document.get("date") as! String))
//                        self.tableView.insertRows(at: [IndexPath(row: self.diarys.count - 1, section: 0)], with: .automatic)
//                    }
//                    if change.type == .modified {
//                        let diaryContent = DiaryContent(id: change.document.documentID, title: change.document.get("title") as! String, content: change.document.get("content") as! String, date: change.document.get("date") as! String)
//                        let index = self.diarys.firstIndex(of: diaryContent) ?? (self.diarys.count - 1)
//                        self.diarys[index] = diaryContent
//                        self.tableView.reloadRows(at: [IndexPath(row: index, section: 0)], with: .automatic)
//                    }
//                    if change.type == .removed {
//                        let diaryContent = DiaryContent(id: change.document.documentID, title: change.document.get("title") as! String, content: change.document.get("content") as! String, date: change.document.get("date") as! String)
//                        if let index = self.diarys.firstIndex(of: diaryContent) {
//                            self.diarys.remove(at: index)
//                            self.tableView.deleteRows(at: [IndexPath(row: index, section: 0)], with: .automatic)
//                        }
//                    }
//                }
                
                self.refreshControl.endRefreshing()
//                self.tableView.reloadData()
            }
        }
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
        
        tableView.delegate = self
        tableView.dataSource = self

        db.collection("diarys").getDocuments(source: .cache) { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                self.diarys = []
                for document in querySnapshot!.documents {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, date: document.get("date") as! String)
                    self.diarys.append(diaryContent)
                }

                self.tableView.reloadData()
            }
        }
        
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(refresh), for: .valueChanged)
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "CalendaryLogo.png")
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
                vc?.diaryId = diarys[row.row].id
                tableView.deselectRow(at: row, animated: true)
            }
        }
    }
 

}
