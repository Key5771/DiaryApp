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
    
    @IBOutlet weak var segment: UISegmentedControl!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    
    let db = Firestore.firestore()
    var diary: [DiaryContent] = [] {
        didSet {
            dataFilter()
        }
    }
    
    enum FilterMethod {
        case popular
        case newSelected
        case oldSelected
        case newWrite
        case oldWrite
    }
    
    var method: FilterMethod = .popular {
        didSet {
            dataFilter()
        }
    }
    
    var filteredDiary: [DiaryContent] = []
    private let refreshControl = UIRefreshControl()
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return filteredDiary.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "shareCell", for: indexPath) as! ShareTableViewCell
        
        cell.titleLabel.text = filteredDiary[indexPath.row].title
        cell.contentLabel.text = filteredDiary[indexPath.row].content
        
        let dateFormat: DateFormatter = DateFormatter()
        dateFormat.dateFormat = "yyyy년 MM월 dd일"
        if segment.selectedSegmentIndex == 1 || segment.selectedSegmentIndex == 0 {
            cell.dateLabel.text = dateFormat.string(from: filteredDiary[indexPath.row].selectTimestamp)
        } else if segment.selectedSegmentIndex == 2 {
            cell.dateLabel.text = dateFormat.string(from: filteredDiary[indexPath.row].timestamp)
        }
        
        return cell
    }
    
    
    @IBAction func selectSegment(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            method = .popular
        } else if sender.selectedSegmentIndex == 1 {
            method = .newSelected
        } else if sender.selectedSegmentIndex == 2 {
            method = .newWrite
        }
    }
    
    func dataFilter() {
        
        switch method {
        case .popular:
            filteredDiary = diary
        case .newSelected:
            filteredDiary = diary.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.selectTimestamp > rhs.selectTimestamp
            })
        case .oldSelected:
            filteredDiary = diary.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.selectTimestamp < rhs.selectTimestamp
            })
        case .newWrite:
            filteredDiary = diary.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.timestamp > rhs.timestamp
            })
        case .oldWrite:
            filteredDiary = diary.sorted(by: { (lhs, rhs) -> Bool in
                return lhs.timestamp < rhs.timestamp
            })
        @unknown default:
            filteredDiary = diary
        }
        
        tableView.reloadData()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        activityIndicatorView.startAnimating()
        
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "Calendary.png")
        imageView.image = image
        navigationItem.titleView = imageView
        
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(refresh), for: .valueChanged)

        // Do any additional setup after loading the view.
    }
    
    @objc func refresh() {
        getDocumentFromFirebase()
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        getDocumentFromFirebase()
    }
    
    func getDocumentFromFirebase() {
        db.collection("Content").whereField("show", isEqualTo: true).getDocuments(completion: { (querySnapshot, error) in
            if let error = error {
                print("Error getting document: \(error)")
            } else {
                self.activityIndicatorView.stopAnimating()
                self.diary = []
                for document in querySnapshot!.documents {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, content: document.get("content") as! String, timestamp: (document.get("timestamp") as! Timestamp).dateValue(), selectTimestamp: (document.get("select timestamp") as! Timestamp).dateValue(), show: (document.get("show") as? String) ?? "", userId: document.get("user id") as! String)
                    self.diary.append(diaryContent)
                }
                self.refreshControl.endRefreshing()
                self.tableView.reloadData()
            }
        })
    }
    
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        
        if segue.identifier == "shareContent" {
            if let row = tableView.indexPathForSelectedRow {
                let vc = segue.destination as? ContentViewController
                vc?.modalPresentationStyle = .overFullScreen
                vc?.diaryId = filteredDiary[row.row].id
                tableView.deselectRow(at: row, animated: true)
            }
        }
    }
    

}
