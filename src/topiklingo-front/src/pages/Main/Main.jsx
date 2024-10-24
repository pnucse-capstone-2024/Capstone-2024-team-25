
import Benefits from "@components/common/benefits";
import Cta from "@components/common/cta";
import { benefitOne, benefitTwo } from "@components/common/data";
import Faq from "@components/common/faq";
import FeatureImg from "@components/common/featureImg";
import Hero from "@components/common/hero";
import SectionTitle from "@components/common/sectionTitle";
import Testimonials from "@components/common/testimonials";
import Video from "@components/common/video";


function Main() {
  return <div><Hero/><SectionTitle
    pretitle="Service Benefits"
    title="Why should you use this service?">
    This service revolutionizes the educational experience through customized learning, innovative AI support, and reduced workload for educators.
  </SectionTitle><Benefits data={benefitOne} /><Benefits imgPos="right" data={benefitTwo} /><FeatureImg/><SectionTitle
    pretitle="About Service"
    title="Precautions for use">
      1. PBT is for <span className="text-red-400">Teacher only</span><br />
      2. CBT is for <span className="text-blue-500">Center and Learner</span><br />
      3. Limit the number of uses
    </SectionTitle><Video videoId="BdBMRN2ph_o?si=ZJoEx_WRgxkC4_yV" /><SectionTitle
      pretitle="Testimonials"
      title="Users&apos; reviews of the service">
      Expected Effect
    </SectionTitle><Testimonials /><SectionTitle pretitle="FAQ" title="Frequently Asked Questions">
      Simple answers to questions users have
    </SectionTitle><Faq /><Cta /></div>;
}

export default Main;
