//
//  AddDiaryViewController.swift
//  Calendary
//
//  Created by 김기현 on 27/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit

class AddDiaryViewController: UIViewController {
    @IBOutlet weak var contentTextview: UITextView!
    @IBOutlet weak var titleTextfield: UITextField!
    @IBOutlet weak var saveButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        titleTextfield.layer.borderWidth = 1
        titleTextfield.layer.borderColor = UIColor.lightGray.cgColor
        titleTextfield.layer.cornerRadius = 10
        
        contentTextview.layer.borderWidth = 1
        contentTextview.layer.borderColor = UIColor.lightGray.cgColor
        contentTextview.layer.cornerRadius = 10
        
        saveButton.layer.cornerRadius = 10

        // Do any additional setup after loading the view.
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
