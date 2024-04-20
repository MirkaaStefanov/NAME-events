package com.example.NAMEevents.Skill;

import com.example.NAMEevents.Event.Event;
import com.example.NAMEevents.Event.EventDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping("/skill")
public class SkillController {
    @Autowired
    SkillRepository skillRepository;

    @GetMapping("/add")
    public String addSkill(Model model) {
        model.addAttribute("skill", new Skill());
        return "skill/skill-form";
    }
    @GetMapping("/all")
    public String allSkills(Model model) {
        Iterable<Skill> allSkills = skillRepository.findAll();
        model.addAttribute("allSkills", allSkills);
        return "/";
    }
    @PostMapping("/submit")
    public String postSkill(@Valid @ModelAttribute Skill skill, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allSkills",skillRepository.findAll());
            return "skill/skill-form";
        } else {
            skillRepository.save(skill);
            model.addAttribute("skill", skill);
            return "skill/all-skills";
        }
    }
}
