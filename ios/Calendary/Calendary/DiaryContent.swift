//
//  DiaryContent.swift
//  Calendary
//
//  Created by 김기현 on 28/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import Foundation

struct DiaryContent {
    var id: String
    var title: String
    var content: String
    var date: String
}

extension DiaryContent: Equatable {
    static func ==(lhs: DiaryContent, rhs: DiaryContent) -> Bool {
        return lhs.id == rhs.id
    }
}
