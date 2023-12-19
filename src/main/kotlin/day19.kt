fun day19 (lines: List<String>) {
    val workflows = parseWorkFlows(lines.filter { it.isNotEmpty() })
    val parts = parsePartRatings(lines.filter { it.isNotEmpty() })
    
    val acceptedPartsRating = parts.filter { isPartAccepted(workflows, it) }. map { it.xRating + it.mRating + it.aRating + it.sRating}.sum()

    println("Day 19 part 1: $acceptedPartsRating")
    println("Day 19 part 2: ")
    println()
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
            
            val destination = step.destination
            when (destination) {
                "A" -> return true
                "R" -> return false
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
                val conditionNumber = Integer.parseInt(step.filter { it.isDigit() })
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

data class WorkflowStep(val condition: Char?, val category: Char?, val conditionNumber: Int?, val destination: String)