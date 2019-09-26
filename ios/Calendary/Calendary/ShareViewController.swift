//
//  ShareViewController.swift
//  Calendary
//
//  Created by 김기현 on 20/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore

class ShareViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    var originData: [String] = ["dasdas", "sssss", "sadasd", "qqqq", "323232"]
    var data: [String] = ["dasdas", "sssss", "sadasd", "qqqq", "323232"]
    
    @IBOutlet weak var tableView: UITableView!
    
    let db = Firestore.firestore()
    var diary: [DiaryContent] = []
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return diary.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "shareCell", for: indexPath) as! ShareTableViewCell
        
        cell.titleLabel.text = diary[indexPath.row].title
        cell.contentLabel.text = diary[indexPath.row].content
        
        let dateFormat: DateFormatter = DateFormatter()
        dateFormat.dateFormat = "yyyy년 MM월 dd일"
        cell.dateLabel.text = dateFormat.string(from: diary[indexPath.row].selectTimestamp)
        
        return cell
    }
    
    
    @IBAction func selectSegment(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 1 {
            data = originData.sorted(by: { (lhs, rhs) -> Bool in
                return lhs > rhs
            })
        } else {
            data = originData
        }
        tableView.reloadData()
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "Calendary.png")
        imageView.image = image
        navigationItem.titleView = imageView

        // Do any additional setup after loading the view.
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        getDocumentFromFirebase()
    }
    
    func getDocumentFromFirebase() {
        db.collection("Content").whereField("show", isEqualTo: "true").getDocuments(completion: { (querySnapshot, error) in
            if let error = error {
                print("Error getting document: \(error)")
            } else {
                self.diary = []
                for document in querySnapshot!.documents {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, timestamp: (document.get("timestamp") as! Timestamp).dateValue(), selectTimestamp: (document.get("select timestamp") as! Timestamp).dateValue(), show: (document.get("show") as? String) ?? "", userId: document.get("user id") as! String)
                    self.diary.append(diaryContent)
                }
                self.tableView.reloadData()
            }
        })
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
