//
//  DoworkContent.swift
//  Calendary
//
//  Created by 김기현 on 2019/10/18.
//  Copyright © 2019 김기현. All rights reserved.
//

import Foundation

struct DoworkContent {
    var id: String
    var userId: String
    var content: String
    var timestamp: Date
}

extension DoworkContent: Equatable {
    static func ==(lhs: DoworkContent, rhs: DoworkContent) -> Bool {
        return lhs.id == rhs.id
    }
}
