var distinctCombinations = 0L

fun day19 (lines: List<String>) {
    val workflows = parseWorkFlows(lines.filter { it.isNotEmpty() })
    val parts = parsePartRatings(lines.filter { it.isNotEmpty() })
    
    val acceptedPartsRating = parts.filter { isPartAccepted(workflows, it) }. map { it.xRating + it.mRating + it.aRating + it.sRating}.sum()

    println("Day 19 part 1: $acceptedPartsRating")
    
    findAcceptedCombinations(1, 4000, 1, 4000, 1, 4000, 1, 4000, "in", workflows)
    
    println("Day 19 part 2: $distinctCombinations")
    println()
}

fun findAcceptedCombinations(
    minX: Long,
    maxX: Long,
    minM: Long,
    maxM: Long,
    minA: Long,
    maxA: Long,
    minS: Long,
    maxS: Long,
    workFlowName: String,
    workflows: MutableMap<String, List<WorkflowStep>>
) {
    if (workFlowName == "A") {
        distinctCombinations += ((minX..maxX).count() * 1L * (minM..maxM).count() * 1L * (minA..maxA).count() * 1L * (minS..maxS).count() * 1L)
        return
    } else if (workFlowName == "R") {
        return
    }
    
    val currentWorkFlow = workflows[workFlowName]!!
    //var noOfAccepted = 1L
    var updatedMinX = minX
    var updatedMaxX = maxX
    var updatedMinM = minM
    var updatedMaxM = maxM
    var updatedMinA = minA
    var updatedMaxA = maxA
    var updatedMinS = minS
    var updatedMaxS = maxS

    for (i in currentWorkFlow.indices) {
        val step = currentWorkFlow[i]

        if (step.condition != null) {
            if (step.condition == '<') {
                if (step.category!! == 'x') {
                    findAcceptedCombinations(updatedMinX, step.conditionNumber!! - 1, updatedMinM, updatedMaxM, updatedMinA, updatedMaxA, updatedMinS, updatedMaxS, step.destination, workflows)
                    updatedMinX = step.conditionNumber
                } else if (step.category == 'm') {
                    findAcceptedCombinations(updatedMinX, updatedMaxX, updatedMinM, step.conditionNumber!! - 1, updatedMinA, updatedMaxA, updatedMinS, updatedMaxS, step.destination, workflows)
                    updatedMinM = step.conditionNumber
                } else if (step.category == 'a') {
                    findAcceptedCombinations(updatedMinX, updatedMaxX, updatedMinM, updatedMaxM, updatedMinA, step.conditionNumber!! - 1, updatedMinS, updatedMaxS, step.destination, workflows)
                    updatedMinA = step.conditionNumber
                } else if (step.category == 's') {
                    findAcceptedCombinations(updatedMinX, updatedMaxX, updatedMinM, updatedMaxM, updatedMinA, updatedMaxA, updatedMinS, step.conditionNumber!! - 1, step.destination, workflows)
                    updatedMinS = step.conditionNumber
                }
                continue
            } else if (step.condition == '>') {
                if (step.category!! == 'x') {
                    findAcceptedCombinations(step.conditionNumber!! + 1, updatedMaxX, updatedMinM, updatedMaxM, updatedMinA, updatedMaxA, updatedMinS, updatedMaxS, step.destination, workflows)
                    updatedMaxX = step.conditionNumber
                } else if (step.category == 'm') {
                    findAcceptedCombinations(updatedMinX, updatedMaxX, step.conditionNumber!! + 1, updatedMaxM, updatedMinA, updatedMaxA, updatedMinS, updatedMaxS, step.destination, workflows)
                    updatedMaxM = step.conditionNumber
                } else if (step.category == 'a') {
                    findAcceptedCombinations(updatedMinX, updatedMaxX, updatedMinM, updatedMaxM, step.conditionNumber!! + 1, updatedMaxA, updatedMinS, updatedMaxS, step.destination, workflows)
                    updatedMaxA = step.conditionNumber
                } else if (step.category == 's') {
                    findAcceptedCombinations(updatedMinX, updatedMaxX, updatedMinM, updatedMaxM, updatedMinA, updatedMaxA, step.conditionNumber!! + 1, updatedMaxS, step.destination, workflows)
                    updatedMaxS = step.conditionNumber
                }
                continue
            }
        } else {
            findAcceptedCombinations(updatedMinX, updatedMaxX, updatedMinM, updatedMaxM, updatedMinA, updatedMaxA, updatedMinS, updatedMaxS, step.destination, workflows)
        }
    }
}

fun isPartAccepted(workflows: MutableMap<String, List<WorkflowStep>>, part: Part): Boolean {
    var currentWorkFlow = workflows["in"]!!
    
    while(true) {
        for (i in currentWorkFlow.indices) {
            val step = currentWorkFlow[i]
            if (step.condition != null) {
                val valueToCheck = when(step.category!!) {
                    'x' -> part.xRating
                    'm' -> part.mRating
                    'a' -> part.aRating
                    else -> part.sRating
                }
                
                if (step.condition == '<' && valueToCheck > step.conditionNumber!!) {
                    continue
                } else if (step.condition == '>' && valueToCheck < step.conditionNumber!!) {
                    continue
                }
            }

            return when (val destination = step.destination) {
                "A" -> true
                "R" -> false
                else -> {
                    currentWorkFlow = workflows[destination]!!
                    break
                }
            }
            
        }
        
    }
}

fun parsePartRatings(lines: List<String>): MutableList<Part> {
    val parts = mutableListOf<Part>()

    lines.filter { it.startsWith("{") }.forEach { line ->
        val matches = Regex("[0-9]+").findAll(line)
        val numbers = matches.map { Integer.parseInt(it.value) }.toList()
        parts.add(Part(numbers[0], numbers[1], numbers[2], numbers[3]))
    }
    
    return parts
}

fun parseWorkFlows(lines: List<String>): MutableMap<String, List<WorkflowStep>> {
    val workflows = mutableMapOf<String, List<WorkflowStep>>()
    
    lines.filterNot { it.startsWith("{") }.map{ it.dropLast(1) }.forEach { line ->
        val workflow = mutableListOf<WorkflowStep>()
        val name = line.split("{")[0]
        val steps = line.split("{")[1].split(",")
        
        steps.forEach { step ->
            if (step.contains(":")) {
                val category = step[0]
                val condition = step[1]
                val conditionNumber = (step.filter { it.isDigit() }.toLong())
                val destination = step.split(":")[1]
                workflow.add(WorkflowStep(condition, category, conditionNumber, destination))
            } else {
                workflow.add(WorkflowStep(null, null, null, step))
            }
        }
        
        workflows[name] = workflow
    }
    
    return workflows
}

data class Part(val xRating: Int, val mRating: Int, val aRating: Int, val sRating: Int)

data class WorkflowStep(val condition: Char?, val category: Char?, val conditionNumber: Long?, val destination: String)