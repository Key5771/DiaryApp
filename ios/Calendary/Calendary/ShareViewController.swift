//
//  ShareViewController.swift
//  Calendary
//
//  Created by 김기현 on 20/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit

class ShareViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var data: [String] = []
    var datas = [["dasdas", "sssss", "qqqq"], ["!212", "#2323", "4334", "4343"], ["ㄴㅁㄴㄴㅁㄴ", "ㅇㄴㅇㄴ"]]
    
    @IBOutlet weak var tableView: UITableView!
    
    
    var diarys: [DiaryContent] = []
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "shareCell", for: indexPath) as! ShareTableViewCell
        
        cell.titleLabel.text = "title"
        cell.contentLabel.text = "content"
        cell.dateLabel.text = "date"
        
        return cell
    }
    
    
    @IBAction func selectSegment(_ sender: UISegmentedControl) {
        data = datas[sender.selectedSegmentIndex]
        tableView.reloadData()
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        data = datas[0]
        
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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
