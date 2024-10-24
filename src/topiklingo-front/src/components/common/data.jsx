
import benefitOneImg from "@assets/benefit-one.png";
import benefitTwoImg from "@assets/benefit-two.png";
import {
  FaceSmileIcon,
  ChartBarSquareIcon,
  CursorArrowRaysIcon,
  DevicePhoneMobileIcon,
  AdjustmentsHorizontalIcon,
  SunIcon,
} from "@heroicons/react/24/solid";

const benefitOne = {
  title: "Features",
  desc: "A comprehensive suite of functionalities designed to enhance educational experiences through technology.",
  image: benefitOneImg,
  bullets: [
    {
      title: "TOPIK test accessibility",
      desc: "Enables students to access mock tests on specific topics, facilitating targeted practice.",
      icon: <FaceSmileIcon />,
    },
    {
      title: "Reduce the burden on teachers and instructors",
      desc: "Automates routine tasks and provides support, allowing teachers to focus on teaching and mentoring.",
      icon: <ChartBarSquareIcon />,
    },
    {
      title: "Provide customized questions for each student",
      desc: "Generates personalized question sets based on each student's learning pace and level.",
      icon: <CursorArrowRaysIcon />,
    },
    {
      title: "Provide PBT and CBT services",
      desc: "Offers both Paper-Based Testing and Computer-Based Testing services to accommodate diverse testing needs.",
      icon: <CursorArrowRaysIcon />,
    },
  ],
};

const benefitTwo = {
  title: "Advantages",
  desc: "Leverages artificial intelligence to revolutionize the learning process, offering unique benefits.",
  image: benefitTwoImg,
  bullets: [
    {
      title: "Automatic question AI generation",
      desc: "Utilizes AI to automatically generate questions, ensuring a vast and varied question pool.",
      icon: <DevicePhoneMobileIcon />,
    },
    {
      title: "Provides AI feedback on student answers",
      desc: "Employs AI algorithms to analyze student answers and provide immediate, constructive feedback.",
      icon: <AdjustmentsHorizontalIcon />,
    },
    {
      title: "Learning the type of frequent incorrect answer",
      desc: "Identifies common misconceptions and errors, enabling tailored remediation and learning strategies.",
      icon: <SunIcon />,
    },
  ],
};


export {benefitOne, benefitTwo};
